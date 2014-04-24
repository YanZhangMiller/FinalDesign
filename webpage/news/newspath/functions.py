__author__ = 'v-zhay'
import datetime, time
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