import json
from django.shortcuts import render
from django.http import HttpResponse
from django.http import JsonResponse
from newsForApp.models import User 
from newsForApp.models import HistoryNews
from newsForApp.models import CollectionNews
from newsForApp.models import Map
from newsForApp.models import FilterWordsMap
from newsForApp.models import ForwardingNews
from newsForApp.models import UserMessage
from newsForApp.models import Image
import os
# Create your views here.

def index(request):
    return render(request, 'exam.html')

class G:
    users = set()

def user(request):
    if request.method == "POST":
        email = request.POST.get("email")
        name = request.POST.get("name")
        password = request.POST.get("password")
        avatar = request.POST.get("avatar")
        followig = request.POST.get("followig")
        User.objects.filter(email = email).update(avatar = avatar, followig = followig)
        return HttpResponse("Success")
        
    if request.method == "GET":
        data = []
        users = User.objects.all()
        for user in users:
            data.append(
                {
                    "email":user.email,
                    "name":user.name,
                    "password":user.password,
                    "avatar":user.avatar,
                    "followig":user.followig,
                }
            )
        jsonData = {"data":data}
        return HttpResponse(json.dumps(jsonData))
        
def userSignIn(request):
    if request.method == 'POST':
        email = request.POST.get("email")
        name = request.POST.get("name")
        password = request.POST.get("password")
        print("signin: "+email+'  '+name+' '+password)  
        if len(User.objects.filter(email=email)) == 0:
            return HttpResponse("Fail")
        userInDB = User.objects.filter(email = email)
        passwordOfUserInDB = userInDB[0].password
        if passwordOfUserInDB != password or userInDB[0].name != name:
            return HttpResponse("Fail")
        if email in G.users:
            return HttpResponse("Fail")
        G.users.add(email)

        data = {
            "avatar":userInDB[0].avatar,
            "followig":userInDB[0].followig,
        }
        return HttpResponse(json.dumps(data))
    return HttpResponse("Fail")

def userSignUp(request):
    if request.method == 'POST':
        email = request.POST.get("email")
        name = request.POST.get("name")
        password = request.POST.get("password")
        if len(User.objects.filter(email=email))!=0:
            return HttpResponse("Fail")

        if email=="" or name == "" or password == "":
            return HttpResponse("Fail")
        newUser = User(email = email, name = name, password = password, followig = "")
        newUser.save()
        G.users.add(email)
        return HttpResponse("Success")
    return HttpResponse("Fail")

def userSignOut(request):
    print("sign out")
    if request.method == "POST":
        G.users.remove(request.POST.get("user"))
        return HttpResponse("Success")
    return HttpResponse("Fail")    

def postAllNews(request):
    if request.method == "POST":
        currentUser = request.GET.get("user")
        HistoryNews.objects.filter(user = currentUser).delete()
        CollectionNews.objects.filter(user = currentUser).delete()
        ForwardingNews.objects.filter(user = currentUser).delete()
        UserMessage.objects.filter(user = currentUser).delete()
        jsonData = json.loads(request.body)
        for data in jsonData["data"]:
            if data["newsType"] == "history":
                newHistoryNews = HistoryNews(newsID = data["newsID"], title = data["title"], date =data["date"], 
                                content = data["content"], person = data["person"], organization =  data["organization"], 
                                location = data["location"], category = data["category"], publisher = data["publisher"],
                                url = data["url"], oriImage = data["image"], oriKeywords = data["image"], 
                                oriScores =  data["scores"], video = data["video"], emails = data["emails"], comments = data["comments"], user = currentUser) 
                newHistoryNews.save()
                print("历史消息保存成功")
            elif data["newsType"] == "collection":
                newCollectionNews = CollectionNews(newsID = data["newsID"], title = data["title"], date =data["date"], 
                                content = data["content"], person = data["person"], organization =  data["organization"], 
                                location = data["location"], category = data["category"], publisher = data["publisher"],
                                url = data["url"], oriImage = data["image"], oriKeywords = data["image"], 
                                oriScores =  data["scores"], video = data["video"], emails = data["emails"], comments = data["comments"],user = currentUser) 
                newCollectionNews.save()
                print("收藏消息保存成功")
            elif data["newsType"] == "forwardingNews":
                newForwardingNews = ForwardingNews(newsID = data["newsID"], title = data["title"], date =data["date"], 
                                content = data["content"], person = data["person"], organization =  data["organization"], 
                                location = data["location"], category = data["category"], publisher = data["publisher"],
                                url = data["url"], oriImage = data["image"], oriKeywords = data["image"], 
                                oriScores =  data["scores"], video = data["video"], emails = data["emails"], comments = data["comments"],user = currentUser) 
                newForwardingNews.save()
                print("转发消息保存成功")
            elif data["newsType"] == "userMessage":
                newUserMessage = UserMessage(newsID = data["newsID"], title = data["title"], date =data["date"], 
                                content = data["content"], person = data["person"], organization =  data["organization"], 
                                location = data["location"], category = data["category"], publisher = data["publisher"],
                                url = data["url"], oriImage = data["image"], oriKeywords = data["image"], 
                                oriScores =  data["scores"], video = data["video"], emails = data["emails"], comments = data["comments"],user = currentUser) 
                newUserMessage.save()
                print("发布消息保存成功")
            elif data["newsType"] == "map":
                entry = Map.objects.filter(user = currentUser)
                if len(entry) == 0:
                    newWeightMap = Map(data = data["mapData"], user = currentUser)
                    newWeightMap.save()
                else:
                    entry[0].data = data["mapData"]
                    entry[0].save()
                
                entry = FilterWordsMap.objects.filter(user = currentUser)
                if len(entry) == 0:
                    filterWordsMap = FilterWordsMap(data = data["filterWords"], user = currentUser)
                    filterWordsMap.save()
                else:
                    entry[0].data = data["filterWords"]
                    entry[0].save()
                print("推荐和屏蔽的关键词保存成功")
        return HttpResponse("Success")
    return HttpResponse("Fail")
        
def getAllNews(request):
    if request.method == 'GET':
        currentUser = request.GET.get("user")
        data = []
        allWeightMap = Map.objects.filter(user = currentUser)
        filterWords = FilterWordsMap.objects.filter(user = currentUser)
        weightMapData = " "
        filterWordsData = " "
        if len(allWeightMap) != 0:
            weightMapData = allWeightMap[0].data
        if len(filterWords) != 0:
            filterWordsData = filterWords[0].data

        
        allCollectionNews = CollectionNews.objects.filter(user = currentUser) # just return history news for current user
        print(len(allCollectionNews))
        for news in allCollectionNews:
            newsIndata = {
                "newsID":news.newsID,
                "title":news.title,
                "date":news.date,
                "content":news.content,
                "person":news.person,
                "organization":news.organization,
                "location":news.location,
                "category":news.category,
                "publisher":news.publisher,
                "url":news.url,
                "oriImage":news.oriImage,
                "oriKeywords":news.oriKeywords,
                "oriScores":news.oriScores,
                "emails":news.emails,
                "comments":news.comments,
                "video":news.video,
                "newsType":"collection",
                "weight":" ",
                "filterWords":" ",
            }
            data.append(newsIndata)
        print("收藏的新闻数" + str(len(data)))

        allHistoryNews = HistoryNews.objects.filter(user = currentUser) # just return history news for current user
        print(len(allHistoryNews))
        for news in allHistoryNews:
            newsIndata = {
                "newsID":news.newsID,
                "title":news.title,
                "date":news.date,
                "content":news.content,
                "person":news.person,
                "organization":news.organization,
                "location":news.location,
                "category":news.category,
                "publisher":news.publisher,
                "url":news.url,
                "oriImage":news.oriImage,
                "oriKeywords":news.oriKeywords,
                "oriScores":news.oriScores,
                "emails":news.emails,
                "comments":news.comments,
                "video":news.video,
                "newsType":"history",
                "weight":" ",
                "filterWords":" ",
            }
            data.append(newsIndata)
        print("收藏的新闻数+历史消息数" + str(len(data)))

        allForwardingnNews = ForwardingNews.objects.all() # just return history news for current user
        for news in allForwardingnNews:
            newsIndata = {
                "newsID":news.newsID,
                "title":news.title,
                "date":news.date,
                "content":news.content,
                "person":news.person,
                "organization":news.organization,
                "location":news.location,
                "category":news.category,
                "publisher":news.publisher,
                "url":news.url,
                "oriImage":news.oriImage,
                "oriKeywords":news.oriKeywords,
                "oriScores":news.oriScores,
                "emails":news.emails,
                "comments":news.comments,
                "video":news.video,
                "newsType":"forwardingNews",
                "weight":" ",
                "filterWords":" ",
            }
            data.append(newsIndata)
        
        allUserMessage = UserMessage.objects.all()
        for news in allUserMessage:
            newsIndata = {
                "newsID":news.newsID,
                "title":news.title,
                "date":news.date,
                "content":news.content,
                "person":news.person,
                "organization":news.organization,
                "location":news.location,
                "category":news.category,
                "publisher":news.publisher,
                "url":news.url,
                "oriImage":news.oriImage,
                "oriKeywords":news.oriKeywords,
                "oriScores":news.oriScores,
                "emails":news.emails,
                "comments":news.comments,
                "video":news.video,
                "newsType":"userMessage",
                "weight":" ",
                "filterWords":" ",
            }
            data.append(newsIndata)

        newsIndata = {
                "newsID":"",
                "title":"",
                "date":"",
                "content":"",
                "person":"",
                "organization":"",
                "location":"",
                "category":"",
                "publisher":"",
                "url":"",
                "oriImage":"",
                "oriKeywords":"",
                "oriScores":"",
                "emails":"",
                "comments":"",
                "video":"",
                "newsType":"map",
                "weight":weightMapData,
                "filterWords":filterWordsData,
            }
        data.append(newsIndata)
        jsonData = {"data":data}
        return HttpResponse(json.dumps(jsonData))

def uploadImage(request):
    source = request.FILES.get('image')
    if source:
      image = Image(img=source, filename = request.POST.get("filename"))
      image.save()
      return HttpResponse("Success")
    else:
      return HttpResponse("Fail")

def downloadImage(request):
    data = request.GET 
    filename = data.get("filename")
    imagepath = os.path.join("media/img/", filename)
    with open(imagepath, "rb") as f:
        image = f.read()
    return HttpResponse(image, content_type="image")
    
