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

<!-- inline scripts related to this page -->
//邀请码列专用的格式:未使用的以超链接格式存在,已使用的只显示内容
$.extend($.fn.fmatter.actions, {
    showLinkFormatter: function (cellval,opts) {
        
    }
});

//状态列专用格式:used显示为已使用,unused显示未使用
$.extend($.fn.fmatter, {
    priceFormatter: function (cellvalue, options, rowdata) {
        //
        cellvalue = cellvalue.replace(/[^\d]/g, "");
        cellvalue = cellvalue - 1 + 1;//"01"->"1"
        return cellvalue;
    }
});

$(function ($) {
    var server_host = "";
    var grid_selector = "#grid-table";
    var pager_selector = "#grid-pager";

    //resize to fit page size
    $(window).on('resize.jqGrid', function () {
        $(grid_selector).jqGrid('setGridWidth', $(".page-content").width());
    });

    $(grid_selector).jqGrid({
        url: server_host + "/admin/list?access_token=" + $accessToken,
        editurl: "/admin/update",
        mtype: "get",
        datatype: "json",
        height: 450,
        emptyrecords: "未检索到相关数据",
        colNames: ['操作', '序号', '服务类型', '单品名称', '单价', '数量', '原价', '套餐价', '创建时间', '修改时间'],
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
            {name: 'serviceName', index: 'serviceName', width: 90, align: "center", sortable: false, search: false},
            {name: 'skuName', index: 'skuName', width: 90, align: "center", sortable: false, search: false}, 
            {name: 'unitPrice', index: 'unitPrice', width: 40, align: "center", sortable: false, search: false}, 
            {name: 'skuAmount', index: 'skuAmount', width: 40, align: "center", sortable: false, search: false}, 
            {name: 'originalPrice', index: 'originalPrice', width: 40, align: "center", sortable: false, search: false}, 
            {name: 'actualPrice', index: 'actualPrice', width: 40, align: "center", sortable: false, search: false,
                editable: true, editrules:{number: true}, formatter:"priceFormatter"}, 
            {name: 'createTime', index: 'createTime', width: 90, align: "center", sortable: false, search: false}, 
            {name: 'updateTime', index: 'updateTime', width: 90, align: "center", sortable: false, search: false}
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
                    $.ajax({
                        url: "add-form.html",
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
            url: "query-form.html",
            success: function (result) {
                $('.modal-dialog').html(result);
                $('#btnQuery').click();
            }
        });
    }

});
