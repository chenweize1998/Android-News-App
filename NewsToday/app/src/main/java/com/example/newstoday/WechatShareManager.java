package com.example.newstoday;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.widget.Toast;

import java.io.*;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.*;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import static com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneSession;


public class WechatShareManager implements IWXAPIEventHandler{

    private static WechatShareManager mInstance;
    private IWXAPI mWxapi;
    private Context mContext;
    private String APPID = "wx09c88c57e084213f";


    private WechatShareManager(Context context){
        this.mContext = context;
        initWechatShare(context);
    }

    public static WechatShareManager getInstance(Context context){
        if(mInstance == null){
            mInstance = new WechatShareManager(context);
        }
        return mInstance;
    }

    public void initWechatShare(Context context){
        if(mWxapi == null){
            mWxapi = WXAPIFactory.createWXAPI(context, APPID, true);
        }
        mWxapi.registerApp(APPID);
    }

    @Override
    public void onReq(BaseReq baseReq){}

    @Override
    public void onResp(BaseResp resp){
        String result;
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "分享成功";
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "取消分享";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "分享被拒绝";
                break;
            default:
                result = "发送返回";
                break;
        }
        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
    }

    public void shareNews(News news) {
//        String text = news.getContent();
        String text = "hello world!";
        //初始化一个WXTextObject对象
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;
        //用WXTextObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = "这是一个微信分享测试";
        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        //transaction字段用于唯一标识一个请求
        req.transaction = System.currentTimeMillis() + "";
        req.message = msg;
        //发送的目标场景， 可以选择发送到会话 WXSceneSession 或者朋友圈 WXSceneTimeline。 默认发送到会话。
        req.scene = WXSceneSession;
        mWxapi.sendReq(req);
    }

    // 需要对图片进行处理，否则微信会在log中输出thumbData检查错误
    private static byte[] getBitmapBytes(Bitmap bitmap, boolean paramBoolean) {
        Bitmap localBitmap = Bitmap.createBitmap(80, 80, Bitmap.Config.RGB_565);
        Canvas localCanvas;
        localCanvas = new Canvas(localBitmap);
        int i;
        int j;
        if (bitmap.getHeight() > bitmap.getWidth()) {
            i = bitmap.getWidth();
            j = bitmap.getWidth();
        } else {
            i = bitmap.getHeight();
            j = bitmap.getHeight();
        }
        while (true) {
            localCanvas.drawBitmap(bitmap, new Rect(0, 0, i, j), new Rect(0, 0,
                    80, 80), null);
            if (paramBoolean)
                bitmap.recycle();
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            localBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    localByteArrayOutputStream);
            localBitmap.recycle();
            byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
            try {
                localByteArrayOutputStream.close();
                return arrayOfByte;
            } catch (Exception e) {
                e.printStackTrace();
            }
            i = bitmap.getHeight();
            j = bitmap.getHeight();
        }
    }


}
