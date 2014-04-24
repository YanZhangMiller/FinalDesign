from django.shortcuts import render
from django.shortcuts import render_to_response
from newspath.functions import *
from django.shortcuts import HttpResponse
from django.db import connection, transaction
# Create your views here.


def search_news(request):
    newsName = request.GET.get("newsname", "")
    table = request.GET.get("table", "site")
    fromDate = request.GET.get("fromdate", 0)
    toDate = request.GET.get("todate", 0)
    if fromDate == 0 or not is_valid_date(fromDate):
        fromDate = today(7)
    if toDate == 0 or not is_valid_date(toDate):
        toDate = today(0)
    result = [fromDate, toDate, table, newsName]

    cursor = connection.cursor()
    for t in getTables():
        if t["site"].startswith(table):
            cursor.execute("select * from " + t[
                "site"] + "  where title like %s and pubtime < %s and pubtime > %s order by pubtime desc limit 5",
                           ["%" + newsName + "%", toDate, fromDate])
            for data in cursor.fetchall():
                result.append(data)
    return render_to_response('search.json', result)


def search_site(request):
    return render_to_response('website.html', {'table': getTables()})


def show_newspath(request):
    return render_to_response('newspath.html', {})