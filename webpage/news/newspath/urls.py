__author__ = 'v-zhay'
from django.conf.urls import patterns, url
from newspath.views import *
urlpatterns = patterns('',
                       url(r'^$', show_newspath),
                       url(r'^search_news$', search_news),
                       url(r'^websites$', search_site),
                       url(r'^search_graph$',search_graph),
)
