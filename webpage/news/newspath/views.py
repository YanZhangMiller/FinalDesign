from django.shortcuts import render

# Create your views here.
def search_news(request):
    from django.db import connection, transaction
    cursor = connection.cursor()
    cursor.excute("SHOW TABLES")
    

