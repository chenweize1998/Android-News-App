import json
from django.shortcuts import render
from django.http import HttpResponse
from django.http import JsonResponse
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
    print("sign out")
    if request.method == "GET":
        HistoryNews.objects.filter(user = G.currentUser).delete()
        G.currentUser = "null"
        return HttpResponse("Success")
    return HttpResponse("Fail")    

def history(request):
    if request.method == 'POST':
        if G.currentUser == 'null':
            return HttpResponse("Fail")
        try:
            if len(HistoryNews.objects.filter(newsID = request.POST["newsID"])) != 0:
                return HttpResponse("Success") 
            newHistoryNews = HistoryNews(newsID = request.POST["newsID"], title = request.POST["title"], date = request.POST["date"], 
                                            content = request.POST["content"], person = request.POST["person"], organization = request.POST["organization"], 
                                            location = request.POST["location"], category = request.POST["category"], publisher = request.POST["publisher"],
                                            url = request.POST["url"], oriImage = request.POST["oriImage"], oriKeywords = request.POST["oriKeywords"], 
                                            oriScores =  request.POST["oriScores"], video = request.POST["video"], user = G.currentUser) 
            newHistoryNews.save()
            print("history post", end='      ')
            print("保存成功")
            return HttpResponse("Success")  
        except KeyError:
            return HttpResponse("Fail")
    return HttpResponse("Fail")
    # if request.method == 'GET':
    #     print(G.currentUser)
    #     if G.currentUser == 'null':
    #         return HttpResponse("Fail")
    #     data = []
    #     allHistoryNews = HistoryNews.objects.filter(user = G.currentUser) # just return history news for current user
    #     for news in allHistoryNews:
    #         newsIndata = {
    #             "newsID":news.newsID,
    #             "title":news.title,
    #             "date":news.date,
    #             "content":news.content,
    #             "person":news.person,
    #             "organization":news.organization,
    #             "location":news.location,
    #             "category":news.category,
    #             "publisher":news.publisher,
    #             "url":news.url,
    #             "oriImage":news.oriImage,
    #             "oriKeywords":news.oriKeywords,
    #             "oriScores":news.oriScores,
    #             "video":news.video
    #         }
    #         data.append(newsIndata)
    #     jsonData = {"data":data}
    #     print("historynews, get:", end='    ')
    #     print(jsonData)
    #     return HttpResponse(str(json.dumps(jsonData)))


def collection(request):
    if request.method == 'POST':
        if G.currentUser == 'null':
            return HttpResponse("Fail")
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
    return HttpResponse("Fail")
    # if request.method == 'GET':
    #     if G.currentUser == 'null':
    #         return HttpResponse("Fail")
    #     data = []
    #     allCollectionNews = CollectionNews.objects.filter(user = G.currentUser) # just return history news for current user
    #     for news in allCollectionNews:
    #         newsIndata = {
    #             "newsID":news.newsID,
    #             "title":news.title,
    #             "date":news.date,
    #             "content":news.content,
    #             "person":news.person,
    #             "organization":news.organization,
    #             "location":news.location,
    #             "category":news.category,
    #             "publisher":news.publisher,
    #             "url":news.url,
    #             "oriImage":news.oriImage,
    #             "oriKeywords":news.oriKeywords,
    #             "oriScores":news.oriScores,
    #             "video":news.video
    #         }
    #         data.append(newsIndata)
    #     jsonData = {"data":data}
    #     return HttpResponse(json.dumps(jsonData))

def weightMap(request):
    # if request.method == 'GET':
    #     if G.currentUser == 'null':
    #         return HttpResponse("Fail")
    #     allWeightMap = Map.objects.filter(user = G.currentUser)
    #     print(allWeightMap[0].data)
    #     return HttpResponse(allWeightMap[0].data)
    if request.method == 'POST':
        if G.currentUser == 'null':
            return HttpResponse("Fail")
        try:
            print("weightMap post:", end='      ')
            print(request.POST)
            entry = Map.objects.filter(user = G.currentUser)
            if len(entry) == 0:
                newWeightMap = Map(data = request.POST["data"], user = G.currentUser)
                newWeightMap.save()
            else:
                entry[0].data = request.POST["data"]
                entry[0].save()
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
                "weight":allWeightMap[0].data
            }
            data.append(newsIndata)
        print(len(data))

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
                "weight":allWeightMap[0].data
            }
            data.append(newsIndata)
        print(len(data))
        
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
                "weight":allWeightMap[0].data
            }
        data.append(newsIndata)
        jsonData = {"data":data}
        # print(jsonData)
        return HttpResponse(json.dumps(jsonData))
