/**
 * Created by malu on 16/7/21.
 */
function getParameter(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return r[2];
    }
    return null;
}

var $role = getParameter("role");
var $accessToken = getParameter("access_token");
var $skuId = "";

//初始化树
$(function ($) {
    var tree_view = "#tree-view";
    var sampleData = initData();//see below
    $(tree_view).ace_tree({
        dataSource: sampleData['dataSource'],
        multiSelect: false,
        // cacheItems: true,
        'open-icon' : 'ace-icon tree-minus',
        'close-icon' : 'ace-icon tree-plus',
        'selectable' : true,
        'selected-icon' : 'icon-ok',
        'unselected-icon' : 'icon-remove',
        loadingHTML : '<div class="tree-loading"><i class="ace-icon fa fa-refresh fa-spin blue"></i></div>'
    });

    /**
     * click事件被封装,使用deselected和selected事件实现click效果
     */
    $(tree_view).on('deselected.fu.tree', function(evt, data) {
        clickedItems(data.target);
    }).on('selected.fu.tree', function (evt, data) {
        clickedItems(data.target);
    });

    /**
     * 单击事件,若点击对象为子节点则进行查询操作
     */
    function clickedItems(target) {
        var item = target;
        var id = "";
        var name = "";

        var parentId = item.additionalParameters['parentId'];
        id = item.additionalParameters['id'];
        name = item.text;
        //父节点(parentId == null)则不进行任何操作
        if(parentId == null){
            return false;
        }
        //设置全局变量serviceId,方便表格数据操作
        $skuId = id;
        //设置表头信息
        $(".ui-jqgrid-title").html(name);
        //加载表数据
        $("#grid-table").jqGrid('setGridParam', {
            datatype: "json",
            url: "/admin/list?access_token=" + $accessToken + "&skuId=" + $skuId,
            page: 1
        }).trigger('reloadGrid');
    }

    //初始化数据
    function initData() {
        var dataSource = function(options, callback){
            var $data = null
            if(!("text" in options) && !("type" in options)){
                $.ajax({
                    url: "/admin/tree/initDate",
                    type: 'get',
                    dataType: 'json',
                    success : function(result) {
                        if(result.returnMsg == "success" && result.returnCode == 200)
                            callback({ data: result.data })
                    },
                    error: function(result) {
                        console.log(result.returnMsg);
                    }
                });
                return;
            }
            else if("type" in options && options.type == "folder") {
                if("additionalParameters" in options && "children" in options.additionalParameters)
                    $data = options.additionalParameters.children || {};
                else $data = {}//no data
            }
            if($data != null)//this setTimeout is only for mimicking some random delay
                setTimeout(function(){callback({ data: $data });} , parseInt(Math.random() * 500) + 200);
        }
        return {'dataSource': dataSource}
    }

    /** -------------------初始化jqGrid------------------ **/

    var server_host = "";
    var grid_selector = "#grid-table";
    var pager_selector = "#grid-pager";

    //resize to fit page size
    $(window).on('resize.jqGrid', function () {
        $(grid_selector).jqGrid('setGridWidth', $(".col-sm-10").width());
    });

    $(grid_selector).jqGrid({
        url: server_host + "/admin/list?access_token=" + $accessToken + "&skuId=" + $skuId,
        editurl: "/admin/update",
        mtype: "get",
        datatype: "json",
        height: 450,
        emptyrecords: "未检索到相关数据",
        colNames: ['操作', '序号', '单价(元)', '数量(次)', '周期(月)', '原价(元)', '套餐价(元)'],
        colModel: [
            {name: 'myac', index: '', width: 40, align: "center", sortable: false, search: false, formatter: "actions",
                formatoptions:
                {
                    keys: true,
                    editOptions: {  //编辑操作，这个很重要，实现编辑时传送参数什么的。  
                        reloadAfterSubmit: true,
                        editData: {}
                    },
                    delOptions: { //删除操作，这个很重要，实现删除时传送参数什么的。  这处网上没有例子的。  
                        url: "/admin/delete",
                        reloadAfterSubmit: true,
                        delData: {}
                    }
                }
            },
            {name: 'id', index: 'id', width: 40, align: "center", sortable: false, search: false},
            {name: 'unitPrice', index: 'unitPrice', width: 40, align: "center", sortable: false, search: false},
            {name: 'skuAmount', index: 'skuAmount', width: 40, align: "center", sortable: false, search: false},
            {name: 'skuCycle', index: 'skuCycle', width: 40, align: "center", sortable: false, search: false},
            {name: 'originalPrice', index: 'originalPrice', width: 40, align: "center", sortable: false, search: false},
            {name: 'actualPrice', index: 'actualPrice', width: 40, align: "center", sortable: false, search: false,
                editable: true, editrules:{number: true}, formatter:"priceFormatter"}
        ],
        sortable: false,
        viewrecords: true,
        rowNum: 20,
        rowList: [10, 20, 30],
        pager: pager_selector,
        altRows: false,
        scrollOffset: 2,
        multiselect: false,
        multiboxonly: true,

        loadComplete: function () {
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
                enableTooltips(table);
            }, 0);
        },

        loadError: function (error) {
            if (error.readyState == 4) {

            }
            window.location.href = "/";
        },

        caption: "Combos Admin",
        autowidth: true
    });
    $(window).triggerHandler('resize.jqGrid');//trigger window resize to make the grid get the correct size

    //navButtons
    $(grid_selector).jqGrid('navGrid', pager_selector,
        { 	//navbar options
            edit: false,
            editicon: 'ace-icon fa fa-pencil blue bigger-160',
            add: false,
            addicon: 'ace-icon fa fa-plus-circle purple bigger-160',
            del: false,
            delicon: 'ace-icon fa fa-trash-o red bigger-160',
            search: false,
            searchicon: 'ace-icon fa fa-search orange bigger-160',
            refresh: true,
            refreshicon: 'ace-icon fa fa-refresh green bigger-160',
            view: false,
            viewicon: 'ace-icon fa fa-search-plus grey bigger-160'
        }
    ).navButtonAdd(pager_selector, {
        caption: "",
        title: "筛选",
        buttonicon: 'ace-icon fa fa-search orange bigger-160',
        onClickButton: function () {
            loadQueryForm();
        },
        position: "last"
    });

    if ($role == "admin") {
        $(grid_selector).jqGrid('navGrid', pager_selector)
            .navButtonAdd(pager_selector, {
                caption: "",
                title: "创建",
                buttonicon: 'ace-icon fa fa-plus-circle purple bigger-160',
                onClickButton: function () {
                    if($skuId == null || $skuId == ""){
                        alert("请选择左侧服务!");
                        return false;
                    }
                    $.ajax({
                        url: "create-form.html?skuId=" + $skuId,
                        success: function (result) {
                            $('.modal-dialog').html(result);
                            $("#btnCreate").click();
                        }
                    });
                },
                position: "last"
            });
    }


    //replace icons with FontAwesome icons like above
    function updatePagerIcons(table) {
        var replacement =
        {
            'ui-icon-seek-first': 'ace-icon fa fa-angle-double-left bigger-140',
            'ui-icon-seek-prev': 'ace-icon fa fa-angle-left bigger-140',
            'ui-icon-seek-next': 'ace-icon fa fa-angle-right bigger-140',
            'ui-icon-seek-end': 'ace-icon fa fa-angle-double-right bigger-140'
        };
        $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
            var icon = $(this);
            var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

            if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
        });
    }

    function enableTooltips(table) {
        $('.navtable .ui-pg-button').tooltip({container: 'body'});
        $(table).find('.ui-pg-div').tooltip({container: 'body'});
    }

    //加载查询条件遮罩层
    function loadQueryForm() {
        $.ajax({
            url: "query-form.html?skuId=" + $skuId,
            success: function (result) {
                $('.modal-dialog').html(result);
                $('#btnQuery').click();
            }
        });
    }

});
