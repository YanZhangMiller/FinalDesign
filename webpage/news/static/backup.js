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