import json
from django.shortcuts import render
from django.http import HttpResponse
from newsForApp.models import User 
from newsForApp.models import HistoryNews
from newsForApp.models import CollectionNews
# Create your views here.

USERS = {"wei10", "h-peng17"}
currentUser = "null"

def index(request):
    return render(request, 'exam.html')

def userSignIn(request):
    global currentUser
    if request.method == 'POST':
        email = request.POST.get("email")
        password = request.POST.get("password")
        print(email)
        print(password)
        ##
        #if user is not in user_list
        if len(User.objects.filter(email=email)) == 0:
            return HttpResponse("Fail")
        ##
        #check password
        userInDB = User.objects.filter(email = email)
        passwordOfUserInDB = userInDB[0].password
        if passwordOfUserInDB != password:
            return HttpResponse("Fail")
        currentUser = email
        return HttpResponse("Success")
    return HttpResponse("Fail")

def userSignUp(request):
    if request.method == 'POST':
        email = request.POST.get("email")
        name = request.POST.get("name")
        password = request.POST.get("password")
        newUser = User(email = email, name = name, password = password)
        newUser.save()
        return HttpResponse("Success")
    return HttpResponse("Fail")

def userSignOut(request):
    global currentUser
    if request.method == "GET":
        HistoryNews.objects.filter(user = currentUser).delete()
        currentUser = "null"
        return HttpResponse("Success")
    return HttpResponse("Fail")    

def history(request):
    global currentUser
    print(currentUser)
    if request.method == 'POST':
        if currentUser == 'null':
            return HttpResponse("Fail")
        try:
            if len(HistoryNews.objects.filter(newsID = request.POST["newsID"])) != 0:
                return HttpResponse("Success") 
            newHistoryNews = HistoryNews(newsID = request.POST["newsID"], title = request.POST["title"], date = request.POST["date"], 
                                            content = request.POST["content"], person = request.POST["person"], organization = request.POST["organization"], 
                                            location = request.POST["location"], category = request.POST["category"], publisher = request.POST["publisher"],
                                            url = request.POST["url"], oriImage = request.POST["oriImage"], oriKeywords = request.POST["oriKeywords"], 
                                            oriScores =  request.POST["oriScores"], video = request.POST["video"], user = currentUser) 
            newHistoryNews.save()
            print("保存成功")
            return HttpResponse("Success")  
        except KeyError:
            return HttpResponse("Fail")
    if request.method == 'GET':
        if currentUser == 'null':
            return HttpResponse("Fail")
        data = []
        allHistoryNews = HistoryNews.objects.filter(user = currentUser) # just return history news for current user
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
        return HttpResponse(json.dumps(jsonData), content_type = "application/json")


def collection(request):
    global currentUser
    print(currentUser)
    if request.method == 'POST':
        if currentUser == 'null':
            return HttpResponse("Fail")
        try:
            if len(CollectionNews.objects.filter(newsID = request.POST["newsID"])) != 0:
                return HttpResponse("Success") 
            newCollectionNews = CollectionNews(newsID = request.POST["newsID"], title = request.POST["title"], date = request.POST["date"], 
                                            content = request.POST["content"], person = request.POST["person"], organization = request.POST["organization"], 
                                            location = request.POST["location"], category = request.POST["category"], publisher = request.POST["publisher"],
                                            url = request.POST["url"], oriImage = request.POST["oriImage"], oriKeywords = request.POST["oriKeywords"], 
                                            oriScores =  request.POST["oriScores"], video = request.POST["video"], user = currentUser) 
            newCollectionNews.save()
            print("保存成功")
            return HttpResponse("Success")  
        except KeyError:
            return HttpResponse("Fail")
    if request.method == 'GET':
        if currentUser == 'null':
            return HttpResponse("Fail")
        data = []
        allCollectionNews = CollectionNews.objects.filter(user = currentUser) # just return history news for current user
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
    global currentUser
    if request.method == 'GET':
        if currentUser == 'null':
            return HttpResponse("Fail")
        allWeightMap = WeightMap.objects.filter(user = currentUser)
        return HttpResponse(allWeightMap)
    if request.method == 'POST':
        if currentUser == 'null':
            return HttpResponse("Fail")
        try:
            entry = WeightMap.objects.get(user = currentUser)
            if len(entry) == 0:
                newWeightMap = WeightMap(data = request.POST["data"], user = currentUser)
                newWeightMap.save()
            else:
                entry.data = request.POST["data"]
                entry.save()
            return HttpResponse("Success")
        except KeyError:
            return HttpResponse("Fail")
