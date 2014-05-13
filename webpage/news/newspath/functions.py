__author__ = 'v-zhay'
import datetime, time
import  json
from django.db import connection, transaction


def getTables():
    cursor = connection.cursor()
    cursor.execute("select * from nameofsite")
    table = []
    for x in cursor.fetchall():
        table.append({"site": x[0], "name": x[1]})
    return table


def today(days):
    return str(datetime.datetime.now() - datetime.timedelta(days=days))


def is_valid_date(str):
    try:
        time.strptime(str, "%Y-%m-%d")
        return True
    except:
        return False


def packJson(data):
    news = []
    for x in data:
        for i in range(0,len(x)):
            try:
                x[i] = json.dumps(x[i])
            except:
                x[i] = json.dumps(x[i].strftime("%Y-%m-%d"))
        news.append({"url": x[0], "title": x[1], "content": x[2], "html": x[3], "pubtime": x[9], "posurl": x[5],
                     "possite": x[6], "fromurl": x[7], "fromsite": x[8],"site" : x[12 ]})
    return news