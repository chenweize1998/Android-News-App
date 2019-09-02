import json
from django.shortcuts import render
from django.http import HttpResponse
from django.http import JsonResponse
from newsForApp.models import User 
from newsForApp.models import HistoryNews
from newsForApp.models import CollectionNews
from newsForApp.models import Map
from newsForApp.models import FilterWordsMap
# Create your views here.

class G:
    currentUser = 'null'

def index(request):
    return render(request, 'exam.html')

def userSignIn(request):
    if request.method == 'POST':
        email = request.POST.get("email")
        name = request.POST.get("name")
        password = request.POST.get("password")
        print("signin: "+email+'  '+name+' '+password)  
        ##
        #if user is not in user_list
        if len(User.objects.filter(email=email)) == 0:
            return HttpResponse("Fail")
        ##
        #check password
        userInDB = User.objects.filter(email = email)
        passwordOfUserInDB = userInDB[0].password
        if passwordOfUserInDB != password or userInDB[0].name != name:
            return HttpResponse("Fail")
        G.currentUser = email
        userInDB[0].oriFollowig = request.POST.get("oriFollowig")
        userInDB[0].avatar = request.POST.get("avatar")
        userInDB[0].save()
        return HttpResponse("Success")
    return HttpResponse("Fail")

def userSignUp(request):
    if request.method == 'POST':
        email = request.POST.get("email")
        name = request.POST.get("name")
        password = request.POST.get("password")
        if email=="" or name == "" or password == "":
            return HttpResponse("Fail")
        newUser = User(email = email, name = name, password = password, oriFollowig = "", avatar = "")
        newUser.save()
        G.currentUser = email
        return HttpResponse("Success")
    return HttpResponse("Fail")

def userSignOut(request):
    print("sign out")
    if request.method == "GET":
        HistoryNews.objects.filter(user = G.currentUser).delete()
        G.currentUser = "null"
        return HttpResponse("Success")
    return HttpResponse("Fail")    


def userMessage(request):
    if request.method == "POST":
        if G.currentUser == 'null':
            return HttpResponse("Fail")
        userMessage.objects.all().delete()
        try:
            messageID = request.POST.get("messageID")
            email = request.POST.get("email")
            content = request.POST.get("content")
            image = request.POST.get("image")
            newUserMessage = UserMessage(messageID = messageID, email = email, content =content, image = image)
            newUserMessage.save()
            print("用户发布消息保存成功")
            return HttpResponse("Success")
        except KeyError:
            return HttpResponse("Fail")
    if request.method == "GET":
        if G.currentUser == 'null':
            return HttpResponse("Fail")
        allUserMessage = userMessage.objects.all()
        data = []
        for userMessage in allUserMessage:
            data.append(
                {
                    "email":userMessage.email,
                    "messageID":userMessage.messageID,
                    "content":userMessage.content,
                    "image":userMessage.image
                }
            )
        jsonData = {"data":data}
        return HttpResponse(json.dumps(jsonData))

def forwardingNews(request):
    if request.method == 'POST':
        if G.currentUser == 'null':
            return HttpResponse("Fail")
        try:
            ForwardingNews.objects.all().delete()
            newForwardingNews = ForwardingNews(newsID = request.POST["newsID"], title = request.POST["title"], date = request.POST["date"], 
                                            content = request.POST["content"], person = request.POST["person"], organization = request.POST["organization"], 
                                            location = request.POST["location"], category = request.POST["category"], publisher = request.POST["publisher"],
                                            url = request.POST["url"], oriImage = request.POST["oriImage"], oriKeywords = request.POST["oriKeywords"], 
                                            oriScores =  request.POST["oriScores"], video = request.POST["video"]) 
            newForwardingNews.save()
            print("用户转发消息保存成功")
            return HttpResponse("Success")  
        except KeyError:
            return HttpResponse("Fail")

    if request.method == 'GET':
        if G.currentUser == 'null':
            return HttpResponse("Fail")
        data = []
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
                "video":news.video,
            }
            data.append(newsIndata)
        jsonData = {"data":data}
        return HttpResponse(json.dumps(jsonData))
    return HttpResponse("Fail")
    





def history(request):
    if request.method == 'POST':
        if G.currentUser == 'null':
            return HttpResponse("Fail")
        try:
            HistoryNews.objects.all().delete()
            newHistoryNews = HistoryNews(newsID = request.POST["newsID"], title = request.POST["title"], date = request.POST["date"], 
                                            content = request.POST["content"], person = request.POST["person"], organization = request.POST["organization"], 
                                            location = request.POST["location"], category = request.POST["category"], publisher = request.POST["publisher"],
                                            url = request.POST["url"], oriImage = request.POST["oriImage"], oriKeywords = request.POST["oriKeywords"], 
                                            oriScores =  request.POST["oriScores"], video = request.POST["video"], user = G.currentUser) 
            newHistoryNews.save()
            print("历史消息保存成功")
            return HttpResponse("Success")  
        except KeyError:
            return HttpResponse("Fail")
    return HttpResponse("Fail")


def collection(request):
    if request.method == 'POST':
        if G.currentUser == 'null':
            return HttpResponse("Fail")
        try:
            CollectionNews.objects.all().delete()
            newCollectionNews = CollectionNews(newsID = request.POST["newsID"], title = request.POST["title"], date = request.POST["date"], 
                                            content = request.POST["content"], person = request.POST["person"], organization = request.POST["organization"], 
                                            location = request.POST["location"], category = request.POST["category"], publisher = request.POST["publisher"],
                                            url = request.POST["url"], oriImage = request.POST["oriImage"], oriKeywords = request.POST["oriKeywords"], 
                                            oriScores =  request.POST["oriScores"], video = request.POST["video"], user = G.currentUser) 
            newCollectionNews.save()
            print("收藏新闻保存成功")
            return HttpResponse("Success")  
        except KeyError:
            return HttpResponse("Fail")
    return HttpResponse("Fail")


def weightMap(request):
    if request.method == 'POST':
        if G.currentUser == 'null':
            return HttpResponse("Fail")
        try:
            entry = Map.objects.filter(user = G.currentUser)
            if len(entry) == 0:
                newWeightMap = Map(data = request.POST["data"], user = G.currentUser)
                newWeightMap.save()
            else:
                entry[0].data = request.POST["data"]
                entry[0].save()
            print("推荐信息保存成功")
            return HttpResponse("Success")
        except KeyError:
            return HttpResponse("Fail")
    return HttpResponse("Fail")


def filterWords(request):
    if request.method == 'POST':
        if G.currentUser == 'null':
            return HttpResponse("Fail")
        try:
            entry = FilterWordsMap.objects.filter(user = G.currentUser)
            if len(entry) == 0:
                filterWordsMap = FilterWordsMap(data = request.POST["data"], user = G.currentUser)
                filterWordsMap.save()
            else:
                entry[0].data = request.POST["data"]
                entry[0].save()
            print("关键词屏蔽信息保存成功")
            return HttpResponse("Success")
        except KeyError:
            return HttpResponse("Fail")
    return HttpResponse("Fail")


def getAllNews(request):
    if request.method == 'GET':
        if G.currentUser == 'null':
            return HttpResponse("Fail")
        
        data = []
        allWeightMap = Map.objects.filter(user = G.currentUser)
        filterWords = FilterWordsMap.objects.filter(user = G.currentUser)
        
        allCollectionNews = CollectionNews.objects.filter(user = G.currentUser) # just return history news for current user
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
                "video":news.video,
                "newsType":"collection",
                "weight":" ",
                "filterWords":" ",
            }
            data.append(newsIndata)
        print("收藏的新闻数" + str(len(data)))

        allHistoryNews = HistoryNews.objects.filter(user = G.currentUser) # just return history news for current user
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
                "video":news.video,
                "newsType":"history",
                "weight":" ",
                "filterWords":" ",
            }
            data.append(newsIndata)
        print("收藏的新闻数+历史消息数" + str(len(data)))
        
        newsIndata = {
                "newsID":" ",
                "title":" ",
                "date":" ",
                "content":" ",
                "person":" ",
                "organization":" ",
                "location":" ",
                "category":" ",
                "publisher":" ",
                "url":" ",
                "oriImage":" ",
                "oriKeywords":" ",
                "oriScores":" ",
                "video":" ",
                "newsType":"weight",
                "weight":allWeightMap[0].data,
                "filterWords":filterWords[0].data,
            }
        data.append(newsIndata)
        jsonData = {"data":data}
        return HttpResponse(json.dumps(jsonData))
