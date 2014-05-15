/**
 * Created by Yan on 2014/3/31.
 */
var baseUrl = "http://127.0.0.1:8000/newspath/",
    graph = {
    "0": ["1","2"]
    },
    nodes = {
    "0":{
        "Title":"新华网"
    },
    "1":{
        "Title":"网易"
    },
    "2":{
        "Title":"腾讯"
    },
    "3":{
        "Title":"3"
    },
    "4":{
        "Title":"4"
    }
    },

    paper,
    website = {},
    newsList = [];

$(function() {
    var show_graph = function(id){
        $.get(baseUrl + "search_graph" ,{"url" : id },function(data){
            graph = data[0];
            nodes = data[1];
            var d = document.getElementById("newspath");
            var tree = getTree(graph);
            var list = getNodeList(tree);
            if (paper == undefined)
                var paper = Raphael(d.offsetLeft, d.offsetTop, d.offsetWidth, d.offsetHeight);
    //console.log(JSON.stringify(list))
            //console.log(JSON.stringify(list))
            //console.log(JSON.stringify(nodes))
            drawTree(paper,list)
            $("#newscontent").html(wrapContent(id));
        },"json")
    };
    show_graph("4");
});
var circles = {},
    text = {},
    connections = [],
    r = 30,
    gap = 5,
    margin = 30;



Raphael.fn.connection = function (obj1, obj2, line, bg) {
    if (obj1.line && obj1.from && obj1.to) {
        line = obj1;
        obj1 = line.from;
        obj2 = line.to;
    }
    var bb1 = obj1.getBBox(),
        bb2 = obj2.getBBox(),
        p = [{x: bb1.x + bb1.width / 2, y: bb1.y - 1},
            {x: bb1.x + bb1.width / 2, y: bb1.y + bb1.height + 1},
            {x: bb1.x - 1, y: bb1.y + bb1.height / 2},
            {x: bb1.x + bb1.width + 1, y: bb1.y + bb1.height / 2},
            {x: bb2.x + bb2.width / 2, y: bb2.y - 1},
            {x: bb2.x + bb2.width / 2, y: bb2.y + bb2.height + 1},
            {x: bb2.x - 1, y: bb2.y + bb2.height / 2},
            {x: bb2.x + bb2.width + 1, y: bb2.y + bb2.height / 2}],
        d = {}, dis = [];
    for (var i = 0; i < 4; i++) {
        for (var j = 4; j < 8; j++) {
            var dx = Math.abs(p[i].x - p[j].x),
                dy = Math.abs(p[i].y - p[j].y);
            if ((i == j - 4) || (((i != 3 && j != 6) || p[i].x < p[j].x) && ((i != 2 && j != 7) || p[i].x > p[j].x) && ((i != 0 && j != 5) || p[i].y > p[j].y) && ((i != 1 && j != 4) || p[i].y < p[j].y))) {
                dis.push(dx + dy);
                d[dis[dis.length - 1]] = [i, j];
            }
        }
    }
    if (dis.length == 0) {
        var res = [0, 4];
    } else {
        res = d[Math.min.apply(Math, dis)];
    }
    var x1 = p[res[0]].x,
        y1 = p[res[0]].y,
        x4 = p[res[1]].x,
        y4 = p[res[1]].y;
    dx = Math.max(Math.abs(x1 - x4) / 2, 10);
    dy = Math.max(Math.abs(y1 - y4) / 2, 10);
    var x2 = [x1, x1, x1 - dx, x1 + dx][res[0]].toFixed(3),
        y2 = [y1 - dy, y1 + dy, y1, y1][res[0]].toFixed(3),
        x3 = [0, 0, 0, 0, x4, x4, x4 - dx, x4 + dx][res[1]].toFixed(3),
        y3 = [0, 0, 0, 0, y1 + dy, y1 - dy, y4, y4][res[1]].toFixed(3);
    var path = ["M", x1.toFixed(3), y1.toFixed(3), "C", x2, y2, x3, y3, x4.toFixed(3), y4.toFixed(3)].join(",");
    if (line && line.line) {
        line.bg && line.bg.attr({path: path});
        line.line.attr({path: path});
    } else {
        var color = typeof line == "string" ? line : "#000";
        return {
            bg: bg && bg.split && this.path(path).attr({stroke: bg.split("|")[0], fill: "none", "stroke-width": bg.split("|")[1] || 3}),
            line: this.path(path).attr({stroke: color, fill: "none"}),
            from: obj1,
            to: obj2
        };
    }
};

var getNodeList = function (tree) {
    var nodeList = [],
        calc = function (nodes, center) {
            if (nodes.length > 0) {
                var alpha = (center.end - center.start ) / (nodes.length + 1.0);
                var x;
                if (nodes.length > 1)
                    x =  (r + gap / 2.0) / Math.sin(alpha / 2.0) ;
                else
                    x = gap + 2 * r;
                if (x < gap + 2 * r)
                    x = gap + 2 * r;

                for (var i = 0; i < nodes.length; i++) {
                    var node = {
                        "x": x * Math.sin(center.start + alpha * (0.25 + i)) + center.x,
                        "y": x * Math.cos(center.start + alpha * (0.25 + i)) + center.y,
                        "r": center.r,
                        "start": center.start + alpha * (i==0?i:i-1),
                        "end": center.start + alpha * (i==length? i + 1 : i + 2),
                        "id": nodes[i].id
                    };
                    nodeList.push(node);
                    calc(nodes[i].childeren, node);
                }
            }
        };
    var node = {"x": 0, "y": 0, "start":0, "end": Math.PI / 2, "id": tree[0].id, "r": r};
    if (tree.length == 1) {
        nodeList.push(node);
        calc(tree[0].childeren, node);
    }
    else if (tree.length > 1) {
        calc(tree, node)
    }
    return nodeList;
};

var getTree = function (graph) {
    var tree = [],
        leaf = [],
        outcome = [];
    for (var i in graph)
    {
        outcome[i] = graph[i].length;
        for (var j in graph[i])
            if (outcome[graph[i][j]] == undefined)
                outcome[graph[i][j]] = 0;
    }
    //document.write(JSON.stringify(outcome) + "<br />")
    var min = 0;
    while (min < outcome.length)
    {
        leaf = tree;
        tree = [];
        min = outcome.length;
        for (var i in outcome) if (outcome[i] < min)
            min = outcome[i];
        //document.write(min + "<br />")
        for (var i in outcome) if (outcome[i] == min) {
            outcome[i] = undefined;
            var node = {};
            //node.data = nodes[i];
            node.id = i;
            node.childeren = [];
            node.number = 1;
            for (var j in graph[i]) for (var k in leaf) if (leaf[k] && leaf[k].id == graph[i][j]) {
                node.childeren.push(leaf[k]);
                node.number += leaf[k].number;
                leaf[k] = undefined;
            }
            tree.push(node);
            for (var j in graph) for (var k in graph[j]) if (graph[j][k] == i)
                outcome[j] -= 1;
        }
        for (var k in leaf) if (leaf[k] != undefined)
            tree.push(leaf[k]);
        //document.write(JSON.stringify(leaf) + "<br />")
        //document.write(JSON.stringify(tree) + "<br />")
    }
    return tree;
}

var drawTree = function (paper,nodeList){
   var top = 0,
       bottom = 0,
       left = 0,
       right = 0,
       h = paper.height,
       w = paper.width;
    paper.clear();
    for (var i in nodeList)
    {
        if (top > nodeList[i].y - r)
            top = nodeList[i].y - r;
        if (bottom < nodeList[i].y + r)
            bottom = nodeList[i].y + r;

        if (left > nodeList[i].x - r)
            left = nodeList[i].x - r;
        if (right < nodeList[i].x + r)
            right = nodeList[i].x + r;
    }
    left -= margin;
    right += margin;
    top -= margin;
    bottom += margin;
    for (var i in nodeList)
    {
        nodeList[i].x = (nodeList[i].x - left) / (right - left) * w;
        nodeList[i].y = (nodeList[i].y - top ) / (bottom - top) * h;
        //nodeList[i].r = nodeList[i].r / ( h / (bottom - top) < w / (right - left) ? (h / (bottom - top)):(w / (right - left)) ) ,
    }
    nodeList = check(nodeList);
    for (var i in nodeList)
        circles[nodeList[i].id] = drawNode(paper,nodeList[i]);
    for (var i in graph)
    {
        for (var j in graph[i])
            connections.push(paper.connection(circles[i],circles[graph[i][j]],"#fff","#000"));
    }
};

var drawNode = function (paper,node){
    var circle = paper.ellipse(node.x,node.y, node.r,node.r),
        color = Raphael.getColor(),
        dragger = function () {
            this.ox = this.type == "rect" ? this.attr("x") : this.attr("cx");
            this.oy = this.type == "rect" ? this.attr("y") : this.attr("cy");
            this.animate({"fill-opacity": .5}, 500);
        },
        move = function (dx, dy) {
            var att = this.type == "rect" ? {x: this.ox + dx, y: this.oy + dy} : {cx: this.ox + dx, cy: this.oy + dy};
            this.attr(att);
            att = {x: this.ox + dx, y: this.oy + dy};
            this.text.attr(att);
            for (var i = connections.length; i--;) {
                paper.connection(connections[i]);
            }
            paper.safari();
        },
        up = function () {
            //this.animate({"fill-opacity": .2}, 500);
        };
    circle.attr({fill: color, stroke: color, "fill-opacity":.2, "stroke-width": 2, cursor: "move"});
    //console.log(node);
    circle.text = paper.text(node.x,node.y,nodes[node.id]["Website"]);
    circle.text.attr({ "font-size": 16,"font-weight": "bold"});
    circle.drag(move,dragger,up);
    circle.mouseup(function(){
        $("#newscontent").html(wrapContent(node.id));
        //this.animate({"fill-opacity": .2}, 1500);
        for(var c in circles)
            circles[c].animate({"fill-opacity":.2},100);
        this.animate({"fill-opacity":.7}, 500);
        //console.log(wrapContent(node.id));
    });
    return circle;
};

var check = function (nodeList,fix){
    if (fix = undefined || fix <0 || fix > nodeList.length)
        fix = 0;
    var dist = function(a,b) {        return Math.sqrt((a.x - b.x)*(a.x- b.x) + (a.y- b.y)*(a.y- b.y))    };
    var space = function(i,j) {
        var d = dist(nodeList[i], nodeList[j]);
        if (d < nodeList[i].r + nodeList[j].r) {
            var sinA = (nodeList[j].y - nodeList[i].y) / d ,
                cosA = (nodeList[j].x - nodeList[i].x) / d;
            nodeList[j].x = nodeList[i].x + (nodeList[i].r + nodeList[j].r + gap) * cosA;
            nodeList[j].y = nodeList[i].y + (nodeList[i].r + nodeList[j].r + gap) * sinA
        }
    };
    for (var i = fix -1 ;i > 0; i--)
    {
        for (var j = i + 1;j<=fix; j++)
        {
            space(j,i);
        }
    }
    for (var i = fix + 1;i <nodeList.length;i++)
    {
        for (var j = i-1; j>=fix ;j--)
            space(j,i)
    }
    return nodeList;
};

var wrapContent = function (id) {

    //console.log(nodes[id])
    //console.log(nodes[id].Author)
    return "<h1>" + nodes[id].Title + "</h1>"
        +"<p>作者:" + nodes[id].Author +"&nbsp;" + nodes[id].ReleaseDateTime + "</p>";
}

var resetConnection = function (paper){
     for (var i = connections.length; i--;) {
                paper.connection(connections[i]);
            }
     paper.safari();
}