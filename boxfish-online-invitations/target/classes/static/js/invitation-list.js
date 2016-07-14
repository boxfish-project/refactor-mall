var $role = $("#role").val();
var $accessToken = $("#accessToken").val();

<!-- inline scripts related to this page -->
//邀请码列专用的格式:未使用的以超链接格式存在,已使用的只显示内容
$.extend($.fn.fmatter, {
    showLinkFormatter: function (cellvalue, options, rowdata) {
        if ("used" == rowdata.statusCode) {
            return cellvalue;
        }
        return "<a class='code-link' data-clipboard-text='" + cellvalue + "' href='#'>" + cellvalue + "</a>";
    }
});

//状态列专用格式:used显示为已使用,unused显示未使用
$.extend($.fn.fmatter, {
    statusFormatter: function (cellvalue, options, rowdata) {
        if ("used" == cellvalue) {
            return "<span class='label label-default arrowed-in-right' style='padding-top:3px'>已使用</span>";
        }
        if ("unused" == cellvalue) {
            return "<span class='label label-success arrowed' style='padding-top:3px'>未使用</span>";
        }
        return "";
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
        url: server_host + "/invitation/list?access_token=" + $accessToken,
        datatype: "json",
        height: 450,
        emptyrecords: "未检索到相关数据",
        colNames: ['序号', '邀请码', '状态', '创建者', '创建时间', '使用者', '使用时间'],
        colModel: [
            {name: 'id', index: 'id', width: 40, align: "center", sortable: false, search: false},
            {
                name: 'content',
                index: 'content',
                width: 150,
                align: "center",
                sortable: false,
                formatter: "showLinkFormatter"
            },
            {
                name: 'statusCode',
                index: 'statusCode',
                width: 40,
                align: "center",
                sortable: false,
                formatter: "statusFormatter"
            },
            {name: 'operator', index: 'operator', width: 40, align: "center", sortable: false, search: false},
            {name: 'createTime', index: 'createTime', width: 90, align: "center", sortable: false, search: false},
            {name: 'userId', index: 'userId', width: 40, align: "center", sortable: false},
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
                showClipboard();
            }, 0);
        },

        caption: "Invitations Admin",
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
        buttonicon: 'ace-icon fa fa-file-excel-o green bigger-160',
        title: "导出",
        onClickButton: function () {
            window.location.href = server_host + "/invitation/export?access_token=" + $accessToken;
        },
        position: "last"
    }).navButtonAdd(pager_selector, {
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
                        url: "create-form.html",
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
                bootbox.dialog({
                    title: "筛选",
                    message: result,
                    buttons: {
                        cancel: {
                            label: "<i class='ace-icon fa fa-times'></i>取消",
                            className: "btn btn-default",
                            callback: function () {
                                $("#notify").addClass("hidden");
                            }
                        },
                        success: {
                            label: "<i class='ace-icon fa fa-search'></i>完成",
                            className: "btn btn-success",
                            callback: function () {
                                var invitation = $('#invitation').val();
                                var userId = $('#userId').val();
                                var status = $("input[name='status']:checked").val();
                                //邀请码/用户格式无误
                                if($("#codeError").text() == "" && $("#userError").text() == ""){
                                    reloadTable(invitation, userId, status);
                                }
                            }
                        }
                    }
                });
            }
        });
    }

    //按查询条件重新加载表格数据
    function reloadTable(p1, p2, p3) {
        $(grid_selector).jqGrid('setGridParam', {
            datatype: "json",
            url: server_host + "/invitation/list?access_token=" + $accessToken + "&content=" + p1 + "&userId=" + p2 + "&statusCode=" + p3,
            page: 1
        }).trigger('reloadGrid');
    }

    function showClipboard() {
        var clipboard = new Clipboard('.code-link');
        clipboard.on('success', function (e) {
            var notify = $("#notify");
            notify.removeClass('hidden');

            $("#share").bind('click', function () {
                window.open(server_host + "/preview.html?access_token=" + $accessToken + "&content=" + e.text);
                notify.addClass('hidden');
            });

            e.clearSelection();
        });
    }
});