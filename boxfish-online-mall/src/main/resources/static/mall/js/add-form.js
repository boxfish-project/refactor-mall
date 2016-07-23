/**
 * Created by malu on 16/7/23.
 */
/**
 * Created by malu on 16/7/19.
 */
function getParameter(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return r[2];
    }
    return null;
}

var $skuId = getParameter("skuId");

//提交按钮
$("#btnSubmit").on('click', function () {
    $("#processing").removeClass("hidden");
    var skuId = $('#skuName').val();//单品类型
    var unitPrice = $('#unitPrice').val();//单品价格
    var skuAmount = $('#skuAmount').val();//单品数量
    var actualPrice = $("#actualPrice").val();//套餐价格
    if(skuId == "" || skuAmount == "" || skuAmount == 0 || actualPrice == ""){
        $("#errorMsg").css("color", "red");
        $("#errorMsg").html("表格数据不能为空!");
        return false;
    }
    var comboVo = '{ "skuId":"' + $skuId + '","skuAmount":"' + skuAmount+ '","actualPrice":"' + actualPrice + '","unitPrice":"' + unitPrice+ '"}';
    $.ajax({
        url: '/admin/create',
        type: 'post',
        contentType: 'application/json',
        dataType: 'json',
        cache: false,
        data: comboVo,
        success: function (result) {
            if(result.data){
                reloadTable("", "", "");
                $("#btnClose").click();
            }
        },
        error: function (errorMsg) {
            console.log(errorMsg);
            $("#errorMsg").css("color", "red");
            //noinspection JSUnresolvedVariable
            $("#errorMsg").html($.parseJSON(errorMsg.responseText).returnMsg);
        }
    });
});

//按查询条件重新加载表格数据
function reloadTable(p1, p2, p3) {
    $("#grid-table").jqGrid('setGridParam', {
        datatype: "json",
        url: "/admin/list?access_token=" + $accessToken + "&serviceName=" + p1 + "&skuName=" + p2 + "&actualPrice=" + p3,
        page: 1
    }).trigger('reloadGrid');
}

//加载数据函数
var setSelectData = function (divId, url) {
    if (divId == "" || url == "") {
        alert("无相应服务!");
        return false;
    }
    $.ajax({
        url:  url,
        type: 'get',
        contentType: 'application/json',
        dataType: 'json',
        cache: false,
        success: function (result) {
            if(divId == "unitPrice"){
                $("#"+divId).val(result.data);
            }else{
                var list = result.data;
                $("#"+divId).html("<option value=''>请选择数据</option>");
                var htmlStr = $("#"+divId).html();

                if(list != ""){
                    $.each(list, function (index, item) {
                        console.log(item);
                        htmlStr += "<option value='"+item.id+"'>"+item.text+"</option>";
                    });
                }
                $("#"+divId).html(htmlStr);
            }
        },
        error: function (errorMsg) {
            console.log(errorMsg);
        }
    });
}

//加载服务类型下拉框数据
setSelectData("serviceName", "/admin/getServiceNameData");

//选择服务类型后set单品类型的选项
$("#serviceName").on("change", function () {
    var serviceId = $("#serviceName").val();
    if(serviceId == ""){
        return false;
    }
    setSelectData("skuName", "/admin/getSkuNameData/"+serviceId);
    console.log(serviceId);
});

//选择单品后设置单品价格
$("#skuName").on("change", function () {
    var skuId = $("#skuName").val();
    if(skuId == ""){
        return false;
    }
    setSelectData("unitPrice", "/admin/queryUnitPrice/"+skuId);
});

//套餐价格选择,所填数据为整数
$("#actualPrice").on("keyup", function () {
    var value = $(this).val();
    value = value.replace(/[^\d]/g,"");
    value = value -1 + 1;//"01"->"1"
    $(this).val(value);
});

//单品数量选择,0-100之间的整数
$("#skuAmount").on("keyup", function () {
    var unitPrice = $("#unitPrice").val();
    var value = $(this).val();
    value = value.replace(/[^\d]/g,"");
    value = value -1 + 1;//"01"->"1"
    if(value >= 100){
        value = 100;
    }
    $(this).val(value);
    $("#actualPrice").val(unitPrice*value);
});

//"-/+"操作
$(".btn-purple").on("click", function () {
    var unitPrice = $("#unitPrice").val();
    var amount = $("#skuAmount").val();
    var id = $(this).attr("id");
    if(id == "btnPlus" && amount >= 2){
        $("#skuAmount").val(amount - 1);
        $("#actualPrice").val(unitPrice*(amount - 1));
    }
    if(id == "btn" && amount <= 9){
        //获取到的amount可能为字符串,"+"处理前进行"-"操作
        $("#skuAmount").val(amount -1 + 2);
        $("#actualPrice").val(unitPrice*(amount - 1 + 2));
    }
});