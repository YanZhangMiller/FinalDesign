/**
 * Created by Yan on 2014/4/1.
 */
var getTree = function (graphe,nodes) {
    var tree = [],
        leaf = [],
        outcome = [];
    for (var i in graphe)
    {
        outcome[i] = graphe[i].length;
        for (var j in graphe[i])
            if (outcome[graphe[i][j]] == undefined)
                outcome[graphe[i][j]] = 0;
    }
    document.write(JSON.stringify(outcome) + "<br />")
    var min = 0;
    while (min < outcome.length)
    {
        leaf = tree;
        tree = [];
        min = outcome.length;
        for (var i in outcome) if (outcome[i] < min)
            min = outcome[i];
        document.write(min + "<br />")
        for (var i in outcome) if (outcome[i] == min) {
            outcome[i] = undefined;
            var node = {};
            node.data = nodes[i];
            node.id = i;
            node.childeren = [];
            node.number = 1;
            for (var j in graphe[i]) for (var k in leaf) if (leaf[k] && leaf[k].id == graphe[i][j]) {
                node.childeren.push(leaf[k]);
                node.number += leaf[k].number;
                leaf[k] = undefined;
            }
            tree.push(node);
            for (var j in graphe) for (var k in graphe[j]) if (graphe[j][k] == i)
                outcome[j] -= 1;
        }
        for (var k in leaf) if (leaf[k] != undefined)
            tree.push(leaf[k]);
        document.write(JSON.stringify(leaf) + "<br />")
        document.write(JSON.stringify(tree) + "<br />")
    }
    return tree;
};

/**
 * Created by Yan on 2014/3/31.
 */
var graphe = {
    "0": ["1","2", "3","4"],
    "4": ["5","6"],
    "2" : ["7"]
};
var nodes = {
    "0":{
        "title":"0",
        "news":[]
    },
    "1":{
        "title":"1"
    },
    "2":{
        "title":"2"
    },
    "3":{
        "title":"3"
    },
    "4":{
        "title":"4"
    },
    "5":{
        "title" : "5"
    },
    "6":{
        "title" : "6"
    },
    "7":{
        "title" : "7"
    }
}


var selectItem = function (){
    if (selected != undefined && circles[selected]!=undefined)
    {
        circles[selected].glow();
    }
};

var dateToString = function (date) {
        return date.getFullYear() + "-" + ("0" + (date.getMonth() + 1)).slice(-2) + "-" + ("0" + date.getDate()).slice(-2)
    },
    liWrapper = function(news){
        return "<li url=\"" + news["url"] + "\">"
            + "<img src=\"/static/pic/logo" + news["site"] + ".png\" />"
            + "<h3>" + news["title"] + "</h3>"
            + "<p>" + news["content"] + "</p>"
            + "<p>来源：" + (news["fromurl"] == news["url"] ? "原创" : news["fromsite"]) + "</p>"
            + "</li>"
    };

/*var getWebsite = function() {
        $.get( baseUrl + "websites", function( data ) {
            website = {}
            for (var i=0;i<data.length;i++){
                website[data[i].site] = {"name":data[i].name,"selected":true}
            }
            var str = "<span site='All'>全部</span>"
            for (var i in website)str += "|<span site='" + i + "'>" + website[i]["name"] + "</span>"
            $("#website").append(str)
            $("#website > span").css("font-weight","bold")
            $("#website > span[site!='All']").click(function(){
                var site = $(this).attr("site")
                website[site].selected = !website[site].selected
                if (website[site].selected){
                    $(this).css("font-weight","bold")
                }
                else
                    $(this).css("font-weight","normal")
                var flag = true
                for (var i in website)
                    if (!website[i].selected && i!='All')
                    {
                        flag = false
                        break
                    }
                website['All'].selected = flag
                if (flag)
                    $("#website span[site='All']").css("font-weight","bold")
                else
                    $("#website span[site='All']").css("font-weight","normal")
                showNews(newsList,true)
            })
            website["All"] = {"selected":true,"name":"全部"}
            $("#website span[site='All']").click(function(){
                website["All"].selected = !website["All"].selected
                if (website["All"].selected)
                {
                    $("#website > span").css("font-weight","bold")
                    for (var i in website) website[i].selected = true
                }
                else
                {
                    $("#website > span").css("font-weight","normal")
                    for (var i in website) website[i].selected = false
                }
                showNews(newsList,true)
            })
        }, "json" );
        },
        showNews = function(news,isclear){
            var ul = $("#newsHtml ul")
            if (isclear)
                ul.empty()
            for (var i = 0;i<news.length;i++)
            {
                if (website[news[i]["site"]].selected)
                    ul.append(liWrapper(news[i]))
            }
            $("#newsHtml ul > li").click(function(){
                alert($(this).attr("url"))
            })

        }
    getWebsite()
    $("#searchBtn").click(function(){
        var from_date = $("#from_date").val(),
            to_date = $("#to_date").val(),
            text = $("#searchText").text()
        $.get(baseUrl + "search_news" ,{"newsname" : text,"fromdate":from_date,"todate":to_date},function(data){
            newsList = data
            showNews(newsList,true)
        },"json")
    })

    var today = new Date()

    $("#to_date").val(dateToString(today))
    today.setDate(today.getDate() - 3)
    $("#from_date").val("2014-01-01")*/