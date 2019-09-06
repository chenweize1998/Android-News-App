from django.db import models

# Create your models here.
class User(models.Model):
    email = models.CharField(max_length=30)
    name = models.CharField(max_length=20)
    password = models.CharField(max_length=20)
    followig = models.CharField(max_length=500)
    avatar = models.CharField(max_length=500)

class Image(models.Model):
    img = models.ImageField(upload_to='img')
    filename = models.CharField(max_length=200)
    time = models.DateTimeField(auto_now_add=True)

class HistoryNews(models.Model):
    newsID = models.CharField(max_length = 100)
    title = models.CharField(max_length = 200)
    date = models.CharField(max_length = 20)
    content = models.CharField(max_length = 5000)
    person = models.CharField(max_length = 200)
    organization = models.CharField(max_length = 200)
    location = models.CharField(max_length = 200)
    category = models.CharField(max_length = 200)
    publisher = models.CharField(max_length = 200)
    url = models.CharField(max_length = 1000)
    oriImage = models.CharField(max_length = 1000)
    oriKeywords = models.CharField(max_length = 200)
    oriScores = models.CharField(max_length = 1000)
    emails = models.CharField(max_length = 500)
    comments = models.CharField(max_length = 2000)
    user = models.CharField(max_length = 200)
    video = models.CharField(max_length = 200)

class CollectionNews(models.Model):
    newsID = models.CharField(max_length = 100)
    title = models.CharField(max_length = 200)
    date = models.CharField(max_length = 20)
    content = models.CharField(max_length = 5000)
    person = models.CharField(max_length = 200)
    organization = models.CharField(max_length = 200)
    location = models.CharField(max_length = 200)
    category = models.CharField(max_length = 200)
    publisher = models.CharField(max_length = 200)
    url = models.CharField(max_length = 1000)
    oriImage = models.CharField(max_length = 1000)
    oriKeywords = models.CharField(max_length = 200)
    oriScores = models.CharField(max_length = 1000)
    emails = models.CharField(max_length = 500)
    comments = models.CharField(max_length = 2000)
    user = models.CharField(max_length = 200)
    video = models.CharField(max_length = 200)

class ForwardingNews(models.Model):
    newsID = models.CharField(max_length = 100)
    title = models.CharField(max_length = 200)
    date = models.CharField(max_length = 20)
    content = models.CharField(max_length = 5000)
    person = models.CharField(max_length = 200)
    organization = models.CharField(max_length = 200)
    location = models.CharField(max_length = 200)
    category = models.CharField(max_length = 200)
    publisher = models.CharField(max_length = 200)
    url = models.CharField(max_length = 1000)
    oriImage = models.CharField(max_length = 1000)
    oriKeywords = models.CharField(max_length = 200)
    oriScores = models.CharField(max_length = 1000)
    emails = models.CharField(max_length = 500)
    comments = models.CharField(max_length = 2000)
    video = models.CharField(max_length = 200)
    user = models.CharField(max_length = 200)

class UserMessage(models.Model):
    newsID = models.CharField(max_length = 100)
    title = models.CharField(max_length = 200)
    date = models.CharField(max_length = 20)
    content = models.CharField(max_length = 5000)
    person = models.CharField(max_length = 200)
    organization = models.CharField(max_length = 200)
    location = models.CharField(max_length = 200)
    category = models.CharField(max_length = 200)
    publisher = models.CharField(max_length = 200)
    url = models.CharField(max_length = 1000)
    oriImage = models.CharField(max_length = 1000)
    oriKeywords = models.CharField(max_length = 200)
    oriScores = models.CharField(max_length = 1000)
    emails = models.CharField(max_length = 500)
    comments = models.CharField(max_length = 2000)
    video = models.CharField(max_length = 200)
    user = models.CharField(max_length = 200)


class Map(models.Model):
    data = models.CharField(max_length = 5000)
    user = models.CharField(max_length = 200)

class FilterWordsMap(models.Model):
    data = models.CharField(max_length = 5000)
    user = models.CharField(max_length = 200)