from django.shortcuts import render
from django.shortcuts import render_to_response
from newspath.functions import *
from django.shortcuts import HttpResponse
from django.db import connection, transaction
import json,copy
# Create your views here.


def search_news(request):
    newsName = request.GET.get("newsname", "")
    fromDate = request.GET.get("fromdate", 0)
    toDate = request.GET.get("todate", 0)

    if fromDate == 0 or not is_valid_date(fromDate):
        fromDate = today(7)
    if toDate == 0 or not is_valid_date(toDate):
        toDate = today(0)
    result = []
    cursor = connection.cursor()
    for t in getTables():
        cursor.execute("select * from " + t[
            "site"] + "  where title like %s and pubtime < %s and pubtime > %s order by pubtime desc limit 1",
                       ["%" + newsName + "%", toDate, fromDate])
        for data in cursor.fetchall():
            data = list(data)
            data.append(t["site"])
            result.append(data)
    return render_to_response('search.html', {"news": packJson(result)})


def search_graph(request):
    url = request.GET.get("url", 0)
    cursor = connection.cursor()
    l = []
    graph = {}
    if url != 0:
        root = url
        cur = url
        while cur:
            cursor.execute("select sourceUrl from newsSource where ID=%s", [cur])
            #print cursor.fetchone()[0]
            try:
                cur = cursor.fetchone()[0]
                if root == cur:
                    break
                if cur:
                    root = cur
            except:
                break
        nodes = [root]
        l.append(root)
        while len(nodes) > 0:
            temp = []
            for x in nodes:
                key = json.dumps(x)
                cursor.execute("select ID from newsSource where sourceUrl=%s", [x])
                for data in cursor.fetchall():
                    value = json.dumps(data[0])
                    temp.append(data[0])
                    l.append(data[0])
                    try:
                        graph[key].append(value)
                    except:
                        graph[key] = [value]
            nodes = temp
        n = get_data(l)
        if len(n) == 1 and n[0]["Source"] != '""':
            n.append(copy.deepcopy(n[0]))
            n[1]["ID"] = json.dumps(json.loads(n[0]["ID"]) + "#")
            n[1]["Website"] = n[0]["Source"]
            n[1]["Source"] = '""'
            graph[n[1]["ID"]] = [n[0]["ID"]]
        if len(graph) == 0:
            graph[json.dumps(root)] = []
    return render_to_response("graph.html", {"graph": graph, "nodes": n})


def get_parent(id):
    cursor = connection.cursor()
    ret = []
    while id != "":
        cursor.execute("select sourceUrl from newsSource where ID=%s", [id])
        try:
            p = cursor.fetchone()[0]
            if p != "":
                ret.append(p)
                id = p
        except:
            break
    return ret


def get_children(id):
    cursor = connection.cursor()
    ret = []
    nodes = [id]
    while nodes.count() > 0:
        temp = []
        for x in nodes:
            cursor.execute("select ID from newsSource where sourceUrl=%s", [x])
            for data in cursor.fetchall():
                temp.append(data[0])
                ret.append(data[0])
        nodes = temp
    return ret


def get_data(l):
    cursor = connection.cursor()
    data = []
    for x in l:
        cursor.execute("select * from newsDetail where ID=%s", [x])
        try:
            row = list(cursor.fetchone())
            for i in range(0, len(row)):
                if not row[i]:
                    row[i] = ""
                try:
                    row[i] = json.dumps(row[i])
                except:
                    row[i] = json.dumps(row[i].strftime("%Y-%m-%d"))
            data.append(
                dict(ID=row[0], Website=row[1], ReleaseDateTime=row[2], Title=row[3], Author=row[4], Content=row[5],
                     Hot=row[6], Source=row[7], UpdateDateTime=row[8], Geo=row[10], Sentiment=row[11], Summary=row[12]))
        except:
            pass
    return data


def search_site(request):
    return render_to_response('website.html', {'table': getTables()})


def show_newspath(request):
    return render_to_response('newspath.html', {})