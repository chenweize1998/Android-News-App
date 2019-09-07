package com.example.newstoday.CustomLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.example.newstoday.Activity.Table;
import com.example.newstoday.News;
import com.example.newstoday.R;

import su.levenetc.android.textsurface.contants.Align;
import su.levenetc.android.textsurface.Text;
import su.levenetc.android.textsurface.TextBuilder;
import su.levenetc.android.textsurface.TextSurface;
import su.levenetc.android.textsurface.animations.Alpha;
import su.levenetc.android.textsurface.animations.ChangeColor;
import su.levenetc.android.textsurface.animations.Circle;
import su.levenetc.android.textsurface.animations.Delay;
import su.levenetc.android.textsurface.animations.Parallel;
import su.levenetc.android.textsurface.animations.Rotate3D;
import su.levenetc.android.textsurface.animations.Sequential;
import su.levenetc.android.textsurface.animations.ShapeReveal;
import su.levenetc.android.textsurface.animations.SideCut;
import su.levenetc.android.textsurface.animations.Slide;
import su.levenetc.android.textsurface.animations.TransSurface;
import su.levenetc.android.textsurface.contants.Direction;
import su.levenetc.android.textsurface.contants.Pivot;
import su.levenetc.android.textsurface.contants.Side;
import su.levenetc.android.textsurface.interfaces.IEndListener;
import su.levenetc.android.textsurface.interfaces.ISurfaceAnimation;

/**
 * Created by Eugene Levenetc.
 */
public class CookieThumperSample {

    public static void play(TextSurface textSurface, Context context, final Activity activity) {
//        final Typeface robotoBlack = Typeface.createFromFile(resources, "font/playfair_display_sc.ttf");
        Typeface typeface = ResourcesCompat.getFont(context, R.font.playfair_display_sc);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTypeface(typeface);

        Text textNewsToday = TextBuilder
                .create("News Today")
                .setPaint(paint)
                .setSize(64)
                .setAlpha(0)
                .setColor(Color.WHITE)
                .setPosition(Align.SURFACE_CENTER).build();

        Text textCodedBy = TextBuilder
                .create("Coded by")
                .setPaint(paint)
                .setSize(44)
                .setAlpha(0)
                .setColor(Color.CYAN)
                .setPosition(Align.BOTTOM_OF, textNewsToday).build();

        Text textWeize = TextBuilder
                .create(" Weize Chen")
                .setPaint(paint)
                .setSize(44)
                .setAlpha(0)
                .setColor(Color.RED)
                .setPosition(Align.RIGHT_OF, textCodedBy).build();

        Text textPengHao = TextBuilder
                .create(" & Hao Peng")
                .setPaint(paint)
                .setSize(44)
                .setAlpha(0)
                .setColor(Color.RED)
                .setPosition(Align.BOTTOM_OF, textWeize).build();

        Text textThreeWeeks = TextBuilder
                .create("3 Weeks")
                .setPaint(paint)
                .setSize(50)
                .setAlpha(0)
                .setColor(Color.RED)
                .setPosition(Align.BOTTOM_OF, textPengHao).build();

        Text text330 = TextBuilder
                .create("330")
                .setPaint(paint)
                .setSize(45)
                .setAlpha(0)
                .setColor(Color.RED)
                .setPosition(Align.BOTTOM_OF | Align.CENTER_OF, textThreeWeeks).build();
//
        Text textCommits = TextBuilder
                .create(" commits")
                .setPaint(paint)
                .setSize(40)
                .setAlpha(0)
                .setColor(Color.WHITE)
                .setPosition(Align.RIGHT_OF, text330).build();

        Text textAddNum = TextBuilder
                .create("10803 ")
                .setPaint(paint)
                .setSize(45)
                .setAlpha(0)
                .setColor(Color.CYAN)
                .setPosition(Align.BOTTOM_OF, text330).build();

        Text textAdd = TextBuilder
                .create(" additions")
                .setPaint(paint)
                .setSize(40)
                .setAlpha(0)
                .setColor(Color.WHITE)
                .setPosition(Align.RIGHT_OF, textAddNum).build();

        Text textJava = TextBuilder
                .create("Android JAVA Project")
                .setPaint(paint)
                .setSize(44)
                .setAlpha(0)
                .setColor(Color.WHITE)
                .setPosition(Align.BOTTOM_OF | Align.CENTER_OF, textAdd).build();

        Text textHope = TextBuilder
                .create("Enjoy it :)")
                .setPaint(paint)
                .setSize(44)
                .setAlpha(0)
                .setColor(Color.CYAN)
                .setPosition(Align.BOTTOM_OF | Align.CENTER_OF, textJava).build();
//
//        Text textSignsInTheAir = TextBuilder
//                .create("signs in the air.")
//                .setPaint(paint)
//                .setSize(44)
//                .setAlpha(0)
//                .setColor(Color.RED)
//                .setPosition(Align.BOTTOM_OF | Align.CENTER_OF, textHope).build();

        textSurface.play(
                new Sequential(
                        ShapeReveal.create(textNewsToday, 750, SideCut.show(Side.LEFT), false),
                        new Parallel(ShapeReveal.create(textNewsToday, 600, SideCut.hide(Side.LEFT), false), new Sequential(Delay.duration(300), ShapeReveal.create(textNewsToday, 600, SideCut.show(Side.LEFT), false))),
                        new Parallel(new TransSurface(500, textCodedBy, Pivot.CENTER), ShapeReveal.create(textCodedBy, 1300, SideCut.show(Side.LEFT), false)),
                        Delay.duration(500),
                        new Parallel(new TransSurface(750, textWeize, Pivot.CENTER), Rotate3D.showFromSide(textWeize, 750, Pivot.LEFT), ChangeColor.to(textWeize, 750, Color.WHITE)),
                        new Parallel(new TransSurface(750, textPengHao, Pivot.CENTER), Rotate3D.showFromSide(textPengHao, 750, Pivot.LEFT), ChangeColor.to(textPengHao, 750, Color.WHITE)),
                        Delay.duration(500),
                        new Parallel(TransSurface.toCenter(textThreeWeeks, 500), Rotate3D.showFromSide(textThreeWeeks, 750, Pivot.TOP)),
                        new Parallel(TransSurface.toCenter(text330, 500), Rotate3D.showFromSide(text330, 500, Pivot.TOP)),
                        new Parallel(new TransSurface(500, textCommits, Pivot.CENTER), ShapeReveal.create(textCommits, 1300, SideCut.show(Side.LEFT), false)),
                        Delay.duration(500),
                        new Parallel(TransSurface.toCenter(textAddNum, 500), Rotate3D.showFromSide(textAddNum, 750, Pivot.TOP)),
                        new Parallel(new TransSurface(500, textAdd, Pivot.CENTER), ShapeReveal.create(textAdd, 1300, SideCut.show(Side.LEFT), false)),
//                        new Parallel(TransSurface.toCenter(textDaaiAnies, 500), Slide.showFrom(Side.TOP, textDaaiAnies, 500)),
//                        new Parallel(TransSurface.toCenter(texThyLamInnie, 750), Slide.showFrom(Side.LEFT, texThyLamInnie, 500))
                        Delay.duration(500),
                        new Parallel(
//                                new TransSurface(1500, textSignsInTheAir, Pivot.CENTER),
                                new Sequential(
                                        new Sequential(ShapeReveal.create(textJava, 500, Circle.show(Side.CENTER, Direction.OUT), false)),
                                        new Sequential(ShapeReveal.create(textHope, 500, Circle.show(Side.CENTER, Direction.OUT), false))
//                                        new Sequential(ShapeReveal.create(textSignsInTheAir, 500, Circle.show(Side.CENTER, Direction.OUT), false))
                                )
                        ),
                        Delay.duration(500),
                        new Parallel(
                                ShapeReveal.create(textJava, 1500, SideCut.hide(Side.LEFT), true),
                                new Sequential(Delay.duration(250), ShapeReveal.create(textHope, 1500, SideCut.hide(Side.LEFT), true)),
//                                new Sequential(Delay.duration(500), ShapeReveal.create(textSignsInTheAir, 1500, SideCut.hide(Side.LEFT), true)),
                                Alpha.hide(textNewsToday, 1500),
                                Alpha.hide(textCodedBy, 1500),
                                Alpha.hide(textWeize, 1500),
                                Alpha.hide(textPengHao, 1500),
                                Alpha.hide(textThreeWeeks, 1500),
                                Alpha.hide(text330, 1500),
                                Alpha.hide(textCommits, 1500),
                                Alpha.hide(textAdd, 1500),
                                Alpha.hide(textAddNum, 1500)
                        ),
                        new Parallel(new ISurfaceAnimation() {
                            @Override
                            public void onStart() {
                                Intent intent = new Intent(activity, Table.class);
                                activity.finish();
                                activity.startActivity(intent);
                                activity.overridePendingTransition(R.xml.fade_in, R.xml.fade_out);
                            }

                            @Override
                            public void start(@Nullable IEndListener iEndListener) {

                            }

                            @Override
                            public void setTextSurface(@NonNull TextSurface textSurface) {

                            }

                            @Override
                            public long getDuration() {
                                return 0;
                            }

                            @Override
                            public void cancel() {

                            }
                        })
                )
        );
    }

}