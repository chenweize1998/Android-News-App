import json
from django.shortcuts import render
from django.http import HttpResponse
from newsForApp.models import User 
from newsForApp.models import HistoryNews
from newsForApp.models import CollectionNews
from newsForApp.models import Map
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
        print(email)
        print(name)
        print(password)
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
        return HttpResponse("Success")
    return HttpResponse("Fail")

def userSignUp(request):
    if request.method == 'POST':
        email = request.POST.get("email")
        name = request.POST.get("name")
        password = request.POST.get("password")
        if email=="" or name == "" or password == "":
            return HttpResponse("Fail")
        newUser = User(email = email, name = name, password = password)
        newUser.save()
        G.currentUser = email
        return HttpResponse("Success")
    return HttpResponse("Fail")

def userSignOut(request):
    if request.method == "GET":
        HistoryNews.objects.filter(user = G.currentUser).delete()
        G.currentUser = "null"
        return HttpResponse("Success")
    return HttpResponse("Fail")    

def history(request):
    print(G.currentUser)
    if request.method == 'POST':
        if G.currentUser == 'null':
            return HttpResponse("Fail")
        HistoryNews.objects.filter(user = G.currentUser).delete()
        try:
            if len(HistoryNews.objects.filter(newsID = request.POST["newsID"])) != 0:
                return HttpResponse("Success") 
            newHistoryNews = HistoryNews(newsID = request.POST["newsID"], title = request.POST["title"], date = request.POST["date"], 
                                            content = request.POST["content"], person = request.POST["person"], organization = request.POST["organization"], 
                                            location = request.POST["location"], category = request.POST["category"], publisher = request.POST["publisher"],
                                            url = request.POST["url"], oriImage = request.POST["oriImage"], oriKeywords = request.POST["oriKeywords"], 
                                            oriScores =  request.POST["oriScores"], video = request.POST["video"], user = G.currentUser) 
            newHistoryNews.save()
            print("保存成功")
            return HttpResponse("Success")  
        except KeyError:
            return HttpResponse("Fail")
    if request.method == 'GET':
        if G.currentUser == 'null':
            return HttpResponse("Fail")
        data = []
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
                "video":news.video
            }
            data.append(newsIndata)
        jsonData = {"data":data}
        print(jsonData)
        return HttpResponse(json.dumps(jsonData), content_type = "application/json")


def collection(request):
    print(G.currentUser)
    if request.method == 'POST':
        if G.currentUser == 'null':
            return HttpResponse("Fail")
        CollectionNews.objects.filter(user = G.currentUser).delete()
        try:
            if len(CollectionNews.objects.filter(newsID = request.POST["newsID"])) != 0:
                return HttpResponse("Success") 
            newCollectionNews = CollectionNews(newsID = request.POST["newsID"], title = request.POST["title"], date = request.POST["date"], 
                                            content = request.POST["content"], person = request.POST["person"], organization = request.POST["organization"], 
                                            location = request.POST["location"], category = request.POST["category"], publisher = request.POST["publisher"],
                                            url = request.POST["url"], oriImage = request.POST["oriImage"], oriKeywords = request.POST["oriKeywords"], 
                                            oriScores =  request.POST["oriScores"], video = request.POST["video"], user = G.currentUser) 
            newCollectionNews.save()
            print("保存成功")
            return HttpResponse("Success")  
        except KeyError:
            return HttpResponse("Fail")
    if request.method == 'GET':
        if G.currentUser == 'null':
            return HttpResponse("Fail")
        data = []
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
                "video":news.video
            }
            data.append(newsIndata)
        jsonData = {"data":data}
        return HttpResponse(json.dumps(jsonData), content_type = "application/json")

def weightMap(request):
    if request.method == 'GET':
        if G.currentUser == 'null':
            return HttpResponse("Fail")
        allWeightMap = Map.objects.filter(user = G.currentUser)
        print(allWeightMap)
        return HttpResponse(allWeightMap)
    if request.method == 'POST':
        if G.currentUser == 'null':
            return HttpResponse("Fail")
        try:
            print("weightMap post:", end='      ')
            print(request.POST)
            entry = Map.objects.get(user = G.currentUser)
            if len(entry) == 0:
                newWeightMap = Map(data = request.POST["data"], user = G.currentUser)
                newWeightMap.save()
            else:
                entry.data = request.POST["data"]
                entry.save()
            return HttpResponse("Success")
        except KeyError:
            return HttpResponse("Fail")
