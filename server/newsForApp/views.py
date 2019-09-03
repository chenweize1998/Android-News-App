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
        if len(User.objects.filter(email=email))!=0:
            return HttpResponse("Fail")

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

def deleteNewsAndMessage(request):
    if request.method == "POST":
        if G.currentUser == 'null':
            return HttpResponse("Fail")
        UserMessage.objects.filter(email = request.POST.get("email")).delete()
        # ForwardingNews.objects.filter(email = request.POST.get("email")).delete()
        HistoryNews.objects.filter(user = request.POST.get("email")).delete()
        CollectionNews.objects.filter(user = request.POST.get("email")).delete()
        return HttpResponse("Success")
    return HttpResponse("Fail")


def userMessage(request):
    if request.method == "POST":
        if G.currentUser == 'null':
            return HttpResponse("Fail")
        # userMessage.objects.all().delete()
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
        allUserMessage =   UserMessage.objects.all()
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

def postAllNews(request):
    if request.method == "POST":
        if G.currentUser == "null":
            return HttpResponse("Fail")
        newsID = request.POST["newsID"].split("#^#")
        title = request.POST["title"].split("#^#")
        date = request.POST["date"].split("#^#")
        content = request.POST["content"].split("#^#")
        person = request.POST["person"].split("#^#")
        organization = request.POST["organization"].split("#^#")
        location = request.POST["location"].split("#^#")
        category = request.POST["category"].split("#^#")
        publisher = request.POST["publisher"].split("#^#")
        url = request.POST["url"].split("#^#")
        oriImage = request.POST["oriImage"].split("#^#")
        oriKeywords = request.POST["oriKeywords"].split("#^#") 
        oriScores =  request.POST["oriScores"].split("#^#")
        video = request.POST["video"].split("#^#")
        newsType = request.POST["newsType"].split("#^#")
        mapData = request.POST["mapData"].split("#^#")
        filterWords = request.POST["filterWords"].split("#^#")

        length = len(newsID)
        print(length)
        print(person)
        print(organization)
        for i in range(length):
            if newsType[i] == "history":
                newHistoryNews = HistoryNews(newsID = newsID[i], title = title[i], date =date[i], 
                                content = content[i], person = person[i], organization = organization[i], 
                                location = location[i], category = category[i], publisher = publisher[i],
                                url = url[i], oriImage = oriImage[i], oriKeywords = oriKeywords[i], 
                                oriScores =  oriScores[i], video = video[i], user = G.currentUser) 
                newHistoryNews.save()
                print("历史消息保存成功")
            elif newsType[i] == "collection":
                newCollectionNews = CollectionNews(newsID = newsID[i], title = title[i], date =date[i], 
                                content = content[i], person = person[i], organization = organization[i], 
                                location = location[i], category = category[i], publisher = publisher[i],
                                url = url[i], oriImage = oriImage[i], oriKeywords = oriKeywords[i], 
                                oriScores =  oriScores[i], video = video[i], user = G.currentUser) 
                newCollectionNews.save()
                print("收藏消息保存成功")
            elif newsType[i] == "forwardingNews":
                newForwardingNews = ForwardingNews(newsID = newsID[i], title = title[i], date =date[i], 
                                content = content[i], person = person[i], organization = organization[i], 
                                location = location[i], category = category[i], publisher = publisher[i],
                                url = url[i], oriImage = oriImage[i], oriKeywords = oriKeywords[i], 
                                oriScores =  oriScores[i], video = video[i]) 
                newForwardingNews.save()
                print("转发消息保存成功")
            elif newsType[i] == "map":
                entry = Map.objects.filter(user = G.currentUser)
                if len(entry) == 0:
                    newWeightMap = Map(data = mapData[i], user = G.currentUser)
                    newWeightMap.save()
                else:
                    entry[0].data = mapData[i]
                    entry[0].save()
                
                entry = FilterWordsMap.objects.filter(user = G.currentUser)
                if len(entry) == 0:
                    filterWordsMap = FilterWordsMap(data = filterWords[i], user = G.currentUser)
                    filterWordsMap.save()
                else:
                    entry[0].data = filterWords[i]
                    entry[0].save()
                print("推荐和屏蔽的关键词保存成功")
        return HttpResponse("Success")
    return HttpResponse("Fail")
        

def getAllNews(request):
    if request.method == 'GET':
        if G.currentUser == 'null':
            return HttpResponse("Fail")
        
        data = []
        allWeightMap = Map.objects.filter(user = G.currentUser)
        filterWords = FilterWordsMap.objects.filter(user = G.currentUser)
        weightMapData = " "
        filterWordsData = " "
        if len(allWeightMap) != 0:
            weightMapData = allWeightMap[0].data
        if len(filterWords) != 0:
            filterWordsData = filterWords[0].data

        
        allCollectionNews = CollectionNews.objects.filter(user = G.currentUser) # just return history news for current user
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
                "video":news.video,
                "newsType":"collection",
                "weight":" ",
                "filterWords":" ",
            }
            data.append(newsIndata)
        print("收藏的新闻数" + str(len(data)))

        allHistoryNews = HistoryNews.objects.filter(user = G.currentUser) # just return history news for current user
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
                "video":news.video,
                "newsType":"forwardingNews",
                "weight":" ",
                "filterWords":" ",
            }
            data.append(newsIndata)

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
                "newsType":"map",
                "weight":weightMapData,
                "filterWords":filterWordsData,
            }
        data.append(newsIndata)
        jsonData = {"data":data}
        return HttpResponse(json.dumps(jsonData))
