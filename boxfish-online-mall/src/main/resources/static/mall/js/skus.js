/**
 * Created by malu on 16/7/26.
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

$(function ($) {
    var server_host = "";
    var grid_selector = "#grid-table-sku";
    var pager_selector = "#grid-pager-sku";

    //resize to fit page size
    $(window).on('resize.jqGrid', function () {
        $(grid_selector).jqGrid('setGridWidth', $(".page-content").width());
    });

    $(grid_selector).jqGrid({
        url: server_host + "/admin/skus/list?access_token=" + $accessToken,
        editurl: "/admin/skus/update",
        mtype: "get",
        datatype: "json",
        height: 320,
        emptyrecords: "未检索到相关数据",
        colNames: ['operate', 'ID', 'serviceName', 'skuName', 'flagEnable', 'flagVisible', 'originalPrice','description'],
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
                        url: "/admin/skus/delete",
                        reloadAfterSubmit: true,
                        delData: {}
                    }
                }
            },
            {name: 'id', index: 'id', width: 40, align: "center", sortable: false, search: false},
            {name: 'serviceName', index: 'serviceName', width: 60, align: "center", sortable: false, search: false},
            {name: 'skuName', index: 'skuName', width: 40, align: "center", sortable: false, search: false, editable: true},
            {name: 'flagEnable', index: 'flagEnable', width: 40, align: "center", sortable: false, search: false,
                editable: true,edittype:"checkbox",editoptions: {value:"ENABLE:DISABLE"},unformat: aceSwitch},
            {name: 'flagVisible', index: 'flagVisible', width: 40, align: "center", sortable: false, search: false,
                editable: true,edittype:"checkbox",editoptions: {value:"ENABLE:DISABLE"},unformat: aceSwitch},
            {name: 'originalPrice', index: 'originalPrice', width: 60, align: "center", sortable: false, search: false,
                editable: true,editrules:{number: true}},
            {name: 'description', index: 'description', width: 90, align: "center", sortable: false, search: false,
                editable: true,edittype:"textarea", editoptions:{rows:"2",cols:"10"}}
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

        caption: "Skus Admin",
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
                        url: "create-form-sku.html",
                        success: function (result) {
                            $('.modal-dialog').html(result);
                            $("#btnCreate").click();
                        }
                    });
                },
                position: "last"
            });
    }






    //switch element when editing inline
    function aceSwitch( cellvalue, options, cell ) {
        setTimeout(function(){
            $(cell) .find('input[type=checkbox]')
                .addClass('ace ace-switch ace-switch-5')
                .after('<span class="lbl"></span>');
        }, 0);
    }
    //enable datepicker
    function pickDate( cellvalue, options, cell ) {
        setTimeout(function(){
            $(cell) .find('input[type=text]')
                .datepicker({dateFormat: 'yyyy-mm-dd', autoclose:true, todayHighlight:true});
        }, 0);
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
            url: "query-form-sku.html",
            success: function (result) {
                $('.modal-dialog').html(result);
                $('#btnQuery').click();
            }
        });
    }

});
