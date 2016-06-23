**1.APP端根据订单id查询订单详情**

    URL:http://localhost:8082/order/app/detail/{order_id}?access_token=xt7jg7KMcp
    method:GET
    param:订单id,access_token
    返回值:{
          "data": {
            "id": 10,
            "createTime": "2016-05-18 11:04:37",
            "updateTime": "2016-05-18 11:04:38",
            "userId": 1000922,
            "code": "100092220160518110437668",
            "totalPrice": 80,
            "status": "WAIT_PAY",
            "orderChannel": "standard",
            "flagDropped": "NO",
            "orderDetails": [
              {
                "id": 78459,
                "createTime": "2016-05-18 11:04:37",
                "updateTime": "2016-05-18 11:04:38",
                "productId": 2,
                "name": "免费体验",
                "originalPrice": 80,
                "price": 80,
                "amount": 1,
                "skuDescription": "{\"id\":2,\"createTime\":\"2016-05-18 10:12:39\",\"updateTime\":null,\"serviceSKU\":{\"id\":1,\"createTime\":\"2016-05-18 10:12:38\",\"updateTime\":null,\"code\":\"ZHONGJIAO\",\"name\":\"中教\",\"countUnit\":null,\"originalPrice\":10,\"description\":\"明星计划7+1\",\"deadline\":\"2016-05-18 10:12:39\",\"type\":1,\"validDay\":30,\"flagEnable\":1,\"flagVisible\":1},\"skuAmount\":8,\"skuCycle\":1,\"actualPrice\":80,\"originalPrice\":80}"
              }
            ]
          },
          "returnCode": 200,
          "returnMsg": "success"
        }

**2.APP端根据订单号查询订单详情**

    URL:/order/app/byCode/{order_code}?access_token=xt7jg7KMcp
    method:GET
    params:订单code,access_token
    return:{
             "data": {
               "id": 1,
               "createTime": "2016-05-18 10:20:29",
               "updateTime": "2016-05-18 10:20:29",
               "userId": 1000922,
               "code": "100092220160518102029149",
               "totalPrice": 0,
               "inviteCode": "sr",
               "status": "PAID",
               "orderSource": "android",
               "orderChannel": "experience",
               "flagDropped": "NO",
               "orderDetails": [
                 {
                   "id": 78446,
                   "createTime": "2016-05-18 10:20:29",
                   "updateTime": "2016-05-18 10:20:29",
                   "productId": 2,
                   "name": "中教授课",
                   "originalPrice": 0,
                   "price": 0,
                   "amount": 2,
                   "skuDescription": "{\"id\":2,\"createTime\":\"2016-05-11 18:34:39\",\"updateTime\":\"2016-05-11 18:34:39\",\"serviceSKU\":{\"id\":4,\"createTime\":\"2016-04-25 15:55:56\",\"updateTime\":\"2016-04-25 15:55:56\",\"code\":\"100201\",\"name\":\"中教授课\",\"countUnit\":20,\"originalPrice\":100.0,\"description\":\"中教在线授课\",\"deadline\":\"2016-04-23 18:22:11\",\"type\":3,\"validDay\":30,\"flagEnable\":1,\"flagVisible\":1},\"skuAmount\":2,\"skuCycle\":-1,\"actualPrice\":0.0,\"originalPrice\":0.0}"
                 },
                 {
                   "id": 78447,
                   "createTime": "2016-05-18 10:20:29",
                   "updateTime": "2016-05-18 10:20:29",
                   "productId": 1,
                   "name": "答疑点评",
                   "originalPrice": 0,
                   "price": 0,
                   "amount": 1,
                   "skuDescription": "{\"id\":1,\"createTime\":\"2016-05-11 18:34:39\",\"updateTime\":\"2016-05-11 18:34:39\",\"serviceSKU\":{\"id\":2,\"createTime\":\"2016-04-25 15:54:44\",\"updateTime\":\"2016-04-25 15:54:44\",\"code\":\"100101\",\"name\":\"答疑点评\",\"countUnit\":10,\"originalPrice\":0.0,\"description\":\"在线答疑点评\",\"deadline\":\"2016-04-23 18:22:04\",\"type\":5,\"validDay\":30,\"flagEnable\":1,\"flagVisible\":1},\"skuAmount\":1,\"skuCycle\":1,\"actualPrice\":0.0,\"originalPrice\":0.0}"
                 }
               ]
             },
             "returnCode": 200,
             "returnMsg": "success"
           }
**3.APP端查询当前用户最近的10个未删除的订单信息**

    URL:/order/app/page/{pageNum}?access_token=xt7jg7KMcp
    method:GET
    params: pageNum,access_token
    return:{
             "data": [
               {
                 "id": 159,
                 "createTime": "2016-05-19 10:07:25",
                 "updateTime": "2016-05-19 10:07:25",
                 "userId": 1000922,
                 "code": "100092220160519100725091",
                 "totalPrice": 80,
                 "status": "WAIT_PAY",
                 "orderChannel": "standard",
                 "flagDropped": "NO",
                 "orderDetails": [
                   {
                     "id": 78609,
                     "createTime": "2016-05-19 10:07:25",
                     "updateTime": "2016-05-19 10:07:25",
                     "productId": 2,
                     "name": "在线中教",
                     "originalPrice": 80,
                     "price": 80,
                     "amount": 1,
                     "skuDescription": "{\"id\":2,\"createTime\":\"2016-05-18 10:12:39\",\"updateTime\":\"2016-05-18 10:12:40\",\"serviceSKU\":{\"id\":1,\"createTime\":\"2016-05-18 10:12:38\",\"updateTime\":\"2016-05-18 10:12:39\",\"code\":\"ZHONGJIAO\",\"name\":\"中教\",\"countUnit\":null,\"originalPrice\":10.0,\"description\":\"明星计划7+1\",\"deadline\":\"2016-05-18 10:12:39\",\"type\":1,\"validDay\":30,\"flagEnable\":1,\"flagVisible\":1},\"skuAmount\":8,\"skuCycle\":1,\"actualPrice\":80.0,\"originalPrice\":80.0}"
                   }
                 ]
               },
               {
                 "id": 155,
                 "createTime": "2016-05-19 10:03:00",
                 "updateTime": "2016-05-19 10:03:00",
                 "userId": 1000922,
                 "code": "100092220160519100300362",
                 "totalPrice": 80,
                 "status": "WAIT_PAY",
                 "orderChannel": "standard",
                 "flagDropped": "NO",
                 "orderDetails": [
                   {
                     "id": 78605,
                     "createTime": "2016-05-19 10:03:00",
                     "updateTime": "2016-05-19 10:03:00",
                     "productId": 2,
                     "name": "在线中教",
                     "originalPrice": 80,
                     "price": 80,
                     "amount": 1,
                     "skuDescription": "{\"id\":2,\"createTime\":\"2016-05-18 10:12:39\",\"updateTime\":\"2016-05-18 10:12:40\",\"serviceSKU\":{\"id\":1,\"createTime\":\"2016-05-18 10:12:38\",\"updateTime\":\"2016-05-18 10:12:39\",\"code\":\"ZHONGJIAO\",\"name\":\"中教\",\"countUnit\":null,\"originalPrice\":10.0,\"description\":\"明星计划7+1\",\"deadline\":\"2016-05-18 10:12:39\",\"type\":1,\"validDay\":30,\"flagEnable\":1,\"flagVisible\":1},\"skuAmount\":8,\"skuCycle\":1,\"actualPrice\":80.0,\"originalPrice\":80.0}"
                   }
                 ]
               },
               {
                 "id": 153,
                 "createTime": "2016-05-19 09:59:28",
                 "updateTime": "2016-05-19 09:59:28",
                 "userId": 1000922,
                 "code": "100092220160519095928772",
                 "totalPrice": 80,
                 "status": "WAIT_PAY",
                 "orderChannel": "standard",
                 "flagDropped": "NO",
                 "orderDetails": [
                   {
                     "id": 78603,
                     "createTime": "2016-05-19 09:59:28",
                     "updateTime": "2016-05-19 09:59:28",
                     "productId": 2,
                     "name": "在线中教",
                     "originalPrice": 80,
                     "price": 80,
                     "amount": 1,
                     "skuDescription": "{\"id\":2,\"createTime\":\"2016-05-18 10:12:39\",\"updateTime\":\"2016-05-18 10:12:40\",\"serviceSKU\":{\"id\":1,\"createTime\":\"2016-05-18 10:12:38\",\"updateTime\":\"2016-05-18 10:12:39\",\"code\":\"ZHONGJIAO\",\"name\":\"中教\",\"countUnit\":null,\"originalPrice\":10.0,\"description\":\"明星计划7+1\",\"deadline\":\"2016-05-18 10:12:39\",\"type\":1,\"validDay\":30,\"flagEnable\":1,\"flagVisible\":1},\"skuAmount\":8,\"skuCycle\":1,\"actualPrice\":80.0,\"originalPrice\":80.0}"
                   }
                 ]
               },
               {
                 "id": 152,
                 "createTime": "2016-05-19 09:59:27",
                 "updateTime": "2016-05-19 09:59:27",
                 "userId": 1000922,
                 "code": "100092220160519095927855",
                 "totalPrice": 80,
                 "status": "WAIT_PAY",
                 "orderChannel": "standard",
                 "flagDropped": "NO",
                 "orderDetails": [
                   {
                     "id": 78602,
                     "createTime": "2016-05-19 09:59:27",
                     "updateTime": "2016-05-19 09:59:27",
                     "productId": 2,
                     "name": "在线中教",
                     "originalPrice": 80,
                     "price": 80,
                     "amount": 1,
                     "skuDescription": "{\"id\":2,\"createTime\":\"2016-05-18 10:12:39\",\"updateTime\":\"2016-05-18 10:12:40\",\"serviceSKU\":{\"id\":1,\"createTime\":\"2016-05-18 10:12:38\",\"updateTime\":\"2016-05-18 10:12:39\",\"code\":\"ZHONGJIAO\",\"name\":\"中教\",\"countUnit\":null,\"originalPrice\":10.0,\"description\":\"明星计划7+1\",\"deadline\":\"2016-05-18 10:12:39\",\"type\":1,\"validDay\":30,\"flagEnable\":1,\"flagVisible\":1},\"skuAmount\":8,\"skuCycle\":1,\"actualPrice\":80.0,\"originalPrice\":80.0}"
                   }
                 ]
               },
               {
                 "id": 151,
                 "createTime": "2016-05-19 09:59:26",
                 "updateTime": "2016-05-19 09:59:26",
                 "userId": 1000922,
                 "code": "100092220160519095926017",
                 "totalPrice": 80,
                 "status": "WAIT_PAY",
                 "orderChannel": "standard",
                 "flagDropped": "NO",
                 "orderDetails": [
                   {
                     "id": 78601,
                     "createTime": "2016-05-19 09:59:26",
                     "updateTime": "2016-05-19 09:59:26",
                     "productId": 2,
                     "name": "在线中教",
                     "originalPrice": 80,
                     "price": 80,
                     "amount": 1,
                     "skuDescription": "{\"id\":2,\"createTime\":\"2016-05-18 10:12:39\",\"updateTime\":\"2016-05-18 10:12:40\",\"serviceSKU\":{\"id\":1,\"createTime\":\"2016-05-18 10:12:38\",\"updateTime\":\"2016-05-18 10:12:39\",\"code\":\"ZHONGJIAO\",\"name\":\"中教\",\"countUnit\":null,\"originalPrice\":10.0,\"description\":\"明星计划7+1\",\"deadline\":\"2016-05-18 10:12:39\",\"type\":1,\"validDay\":30,\"flagEnable\":1,\"flagVisible\":1},\"skuAmount\":8,\"skuCycle\":1,\"actualPrice\":80.0,\"originalPrice\":80.0}"
                   }
                 ]
               },
               {
                 "id": 150,
                 "createTime": "2016-05-19 09:59:19",
                 "updateTime": "2016-05-19 09:59:20",
                 "userId": 1000922,
                 "code": "100092220160519095919695",
                 "totalPrice": 80,
                 "status": "WAIT_PAY",
                 "orderChannel": "standard",
                 "flagDropped": "NO",
                 "orderDetails": [
                   {
                     "id": 78600,
                     "createTime": "2016-05-19 09:59:19",
                     "updateTime": "2016-05-19 09:59:20",
                     "productId": 2,
                     "name": "在线中教",
                     "originalPrice": 80,
                     "price": 80,
                     "amount": 1,
                     "skuDescription": "{\"id\":2,\"createTime\":\"2016-05-18 10:12:39\",\"updateTime\":\"2016-05-18 10:12:40\",\"serviceSKU\":{\"id\":1,\"createTime\":\"2016-05-18 10:12:38\",\"updateTime\":\"2016-05-18 10:12:39\",\"code\":\"ZHONGJIAO\",\"name\":\"中教\",\"countUnit\":null,\"originalPrice\":10.0,\"description\":\"明星计划7+1\",\"deadline\":\"2016-05-18 10:12:39\",\"type\":1,\"validDay\":30,\"flagEnable\":1,\"flagVisible\":1},\"skuAmount\":8,\"skuCycle\":1,\"actualPrice\":80.0,\"originalPrice\":80.0}"
                   }
                 ]
               },
               {
                 "id": 140,
                 "createTime": "2016-05-19 09:42:20",
                 "updateTime": "2016-05-19 09:42:20",
                 "userId": 1000922,
                 "code": "100092220160519094220864",
                 "totalPrice": 80,
                 "status": "WAIT_PAY",
                 "orderChannel": "standard",
                 "flagDropped": "NO",
                 "orderDetails": [
                   {
                     "id": 78590,
                     "createTime": "2016-05-19 09:42:20",
                     "updateTime": "2016-05-19 09:42:20",
                     "productId": 2,
                     "name": "在线中教",
                     "originalPrice": 80,
                     "price": 80,
                     "amount": 1,
                     "skuDescription": "{\"id\":2,\"createTime\":\"2016-05-18 10:12:39\",\"updateTime\":\"2016-05-18 10:12:40\",\"serviceSKU\":{\"id\":1,\"createTime\":\"2016-05-18 10:12:38\",\"updateTime\":\"2016-05-18 10:12:39\",\"code\":\"ZHONGJIAO\",\"name\":\"中教\",\"countUnit\":null,\"originalPrice\":10.0,\"description\":\"明星计划7+1\",\"deadline\":\"2016-05-18 10:12:39\",\"type\":1,\"validDay\":30,\"flagEnable\":1,\"flagVisible\":1},\"skuAmount\":8,\"skuCycle\":1,\"actualPrice\":80.0,\"originalPrice\":80.0}"
                   }
                 ]
               },
               {
                 "id": 139,
                 "createTime": "2016-05-19 09:42:19",
                 "updateTime": "2016-05-19 09:42:20",
                 "userId": 1000922,
                 "code": "100092220160519094219999",
                 "totalPrice": 80,
                 "status": "WAIT_PAY",
                 "orderChannel": "standard",
                 "flagDropped": "NO",
                 "orderDetails": [
                   {
                     "id": 78589,
                     "createTime": "2016-05-19 09:42:19",
                     "updateTime": "2016-05-19 09:42:20",
                     "productId": 2,
                     "name": "在线中教",
                     "originalPrice": 80,
                     "price": 80,
                     "amount": 1,
                     "skuDescription": "{\"id\":2,\"createTime\":\"2016-05-18 10:12:39\",\"updateTime\":\"2016-05-18 10:12:40\",\"serviceSKU\":{\"id\":1,\"createTime\":\"2016-05-18 10:12:38\",\"updateTime\":\"2016-05-18 10:12:39\",\"code\":\"ZHONGJIAO\",\"name\":\"中教\",\"countUnit\":null,\"originalPrice\":10.0,\"description\":\"明星计划7+1\",\"deadline\":\"2016-05-18 10:12:39\",\"type\":1,\"validDay\":30,\"flagEnable\":1,\"flagVisible\":1},\"skuAmount\":8,\"skuCycle\":1,\"actualPrice\":80.0,\"originalPrice\":80.0}"
                   }
                 ]
               },
               {
                 "id": 138,
                 "createTime": "2016-05-19 09:42:18",
                 "updateTime": "2016-05-19 09:42:18",
                 "userId": 1000922,
                 "code": "100092220160519094218939",
                 "totalPrice": 80,
                 "status": "WAIT_PAY",
                 "orderChannel": "standard",
                 "flagDropped": "NO",
                 "orderDetails": [
                   {
                     "id": 78588,
                     "createTime": "2016-05-19 09:42:18",
                     "updateTime": "2016-05-19 09:42:18",
                     "productId": 2,
                     "name": "在线中教",
                     "originalPrice": 80,
                     "price": 80,
                     "amount": 1,
                     "skuDescription": "{\"id\":2,\"createTime\":\"2016-05-18 10:12:39\",\"updateTime\":\"2016-05-18 10:12:40\",\"serviceSKU\":{\"id\":1,\"createTime\":\"2016-05-18 10:12:38\",\"updateTime\":\"2016-05-18 10:12:39\",\"code\":\"ZHONGJIAO\",\"name\":\"中教\",\"countUnit\":null,\"originalPrice\":10.0,\"description\":\"明星计划7+1\",\"deadline\":\"2016-05-18 10:12:39\",\"type\":1,\"validDay\":30,\"flagEnable\":1,\"flagVisible\":1},\"skuAmount\":8,\"skuCycle\":1,\"actualPrice\":80.0,\"originalPrice\":80.0}"
                   }
                 ]
               },
               {
                 "id": 137,
                 "createTime": "2016-05-19 09:42:11",
                 "updateTime": "2016-05-19 09:42:11",
                 "userId": 1000922,
                 "code": "100092220160519094211694",
                 "totalPrice": 80,
                 "status": "WAIT_PAY",
                 "orderChannel": "standard",
                 "flagDropped": "NO",
                 "orderDetails": [
                   {
                     "id": 78587,
                     "createTime": "2016-05-19 09:42:11",
                     "updateTime": "2016-05-19 09:42:11",
                     "productId": 2,
                     "name": "在线中教",
                     "originalPrice": 80,
                     "price": 80,
                     "amount": 1,
                     "skuDescription": "{\"id\":2,\"createTime\":\"2016-05-18 10:12:39\",\"updateTime\":\"2016-05-18 10:12:40\",\"serviceSKU\":{\"id\":1,\"createTime\":\"2016-05-18 10:12:38\",\"updateTime\":\"2016-05-18 10:12:39\",\"code\":\"ZHONGJIAO\",\"name\":\"中教\",\"countUnit\":null,\"originalPrice\":10.0,\"description\":\"明星计划7+1\",\"deadline\":\"2016-05-18 10:12:39\",\"type\":1,\"validDay\":30,\"flagEnable\":1,\"flagVisible\":1},\"skuAmount\":8,\"skuCycle\":1,\"actualPrice\":80.0,\"originalPrice\":80.0}"
                   }
                 ]
               }
             ],
             "returnCode": 200,
             "returnMsg": "success"
           }

**4.创建订单**

     URL:/order/app/create?access_token=xt7jg7KMcp
     method:POST
     data-type:JSON
     data:{
              "userId": 1000922,
              "orderChannel":"standard",
              "inviteCode":"BFPJ5LT3Q4TT9BQ8HU64",
              "orderSource":"android",
              "orderDetails": [
                {
                  "productId": 2,
                  "price": 80,
                  "name":"在线中教",
                  "amount": 1
                }
              ]
          }
     return:{
              "data": {
                "id": 372,
                "createTime": "2016-05-19 21:08:41",
                "userId": 1000922,
                "code": "100092220160519210841749",
                "totalPrice": 80,
                "status": "WAIT_PAY",
                "orderChannel": "standard",
                "flagDropped": "NO",
                "orderDetails": [
                  {
                    "id": 78822,
                    "createTime": "2016-05-19 21:08:41",
                    "productId": 2,
                    "name": "在线中教",
                    "originalPrice": 80,
                    "price": 80,
                    "amount": 1,
                    "skuDescription": "{\"id\":2,\"createTime\":\"2016-05-18 10:12:39\",\"updateTime\":\"2016-05-18 10:12:40\",\"serviceSKU\":{\"id\":1,\"createTime\":\"2016-05-18 10:12:38\",\"updateTime\":\"2016-05-18 10:12:39\",\"code\":\"ZHONGJIAO\",\"name\":\"中教\",\"countUnit\":null,\"originalPrice\":10.0,\"description\":\"明星计划7+1\",\"deadline\":\"2016-05-18 10:12:39\",\"type\":1,\"validDay\":30,\"flagEnable\":1,\"flagVisible\":1},\"skuAmount\":8,\"skuCycle\":1,\"actualPrice\":80.0,\"originalPrice\":80.0}"
                  }
                ]
              },
              "returnCode": 200,
              "returnMsg": "success"
            }

**5.删除订单**

    URL:/order/app/drop/{oid}?access_token=xt7jg7KMcp
    method:DELETE
    params:oid-订单id
    return: 无

**6.更新订单为已支付状态**

    URL:/order/update
    method: POST
    params: {
                "code":xxxxxxx,
                "payChannel": "wechat"/"alipay"/"parent"
            }
    return: {
                "data":"ok",
                "returnCode":200,
                "returnMsg":"success"
            }

