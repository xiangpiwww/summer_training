<template>
    <div>
        <div class="key-input">
            <el-input placeholder="请输入你要搜索的关键词" v-model="searchkey"></el-input>
        </div>

        <div>
            <el-input placeholder="选中文件夹的url地址" v-model="input" :disabled="true"></el-input>
        </div>

        <div class="file-choose-table">
            <el-table :data="tableData" stripe style="width: 100%" @row-click="tableItemClick">
                <el-table-column prop="path" label="文件夹" width="800" ></el-table-column>
                <el-table-column prop="chooseFolder" label="选择" width="750">
                    <!-- <el-radio v-model="radio" :label="id">选择</el-radio> -->
                    <!-- <button @click="tableButtonClick">选择</button> -->
                    <el-button type="primary" @click="tableButtonClick">选择</el-button>
                    <!-- <el-button type="primary" :loading="true" @click="tableButtonClick">加载中</el-button> -->
                </el-table-column>
                <!-- <el-table-column prop="address" label="地址"></el-table-column> -->
        </el-table>
        </div>


        <div class="two-button">
            <!-- <button @click="submitButton()">提交</button> -->
            <el-button type="primary" @click="submitButton()">提交</el-button>
            <el-button type="primary"  @click="backToChoose()">返回上一级</el-button>
        </div>

        <!-- <div> -->
            <!-- <button @click="backToChoose()">返回上一级</button> -->
            
        <!-- </div> -->
        <!-- 展示从后端搜索出来的数据 -->
        
        <hr class="new1">
        <div class="ct">
            <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox>
            <div style="margin: 15px 0;"></div> 
            <el-checkbox-group v-model="checkedFlies" @change="handleCheckedChange">
                <div v-for="file in fileSearchList"  :key="file.id" class="show-checkbox">
                    <el-checkbox  :label="file.id">{{ file.filename }}</el-checkbox>
                    <hr class="new1">
                    <div v-for="data in file.text" :key="data.line">
                        <span class="sep">行号：{{ data.line }}</span>
                        <!-- <span>&nbsp &nbsp &nbsp</span> -->
                        <span class="sep">{{ data.content }}</span>
                    </div>
                    <!-- <hr class="new1"> -->
                    <!-- <div> </div> -->
                </div>
                
            </el-checkbox-group> 
        </div>
        
        <div>
            <!-- <button @click="download()">下载</button> -->
            <el-button type="primary"  @click="download()">下载</el-button>
        </div>
        
    </div>
</template>

<script>
    import axiox from "axios"
    var id = 0;
    var currentArray = []
    var searchback = []
    export default {
        created:function(){
            // 调用接口获取服务器的盘符
            axiox.get("http://localhost:8888/getDir?dir=getInit").then((response)=>{
                // console.log("mxw")
                currentArray = response.data
                //去除 末尾 的 \\
                for(var i=0;i<currentArray.length;i++){
                    currentArray[i] = currentArray[i].slice(0,currentArray[i].indexOf(":")+1)
                }
                console.log("currentArray")
                console.log(currentArray)
                var tableData = []
                for(let i=0;i<currentArray.length;i++){
                    let a = {}
                    a.path = currentArray[i]
                    tableData.push(a)
                }
                console.log(tableData)
                console.log(this.tableData)
                this.tableData = tableData
                //将获取的数据放到currentArray里
            })
        },
        data:function(){
            return{
                checkAll: false,
                isIndeterminate: false,
                checkedFlies: [],
                flag: 0,
                id: ++id,
                showlist: [],
                tableData: [],
                input: '',
                searchkey:"",
                fileSearchList: [],
                // [{
                //         id:1,
                //         filename:'zhy',
                //         text:[
                //             {line:"1",content:"123"},
                //             {line:"2",content:"123"},
                //             {line:"3",content:"123"},
                //     ]},
                //     {
                //         id:2,
                //         filename:'zhy',
                //         text:[
                //             {line:"1",content:"123"},
                //             {line:"2",content:"123"},
                //             {line:"3",content:"123"},
                //     ]}
                // ],
                choose: "", 
            }
        },
        methods:{
            tableButtonClick:function(){
                // table中的button的点击事件
                // 将路径更新input
                // 1、获取点击的按钮的在表格的位置，上传即可
                // 获取当前点击的位置
                // console.log(e)
                this.flag = 1
            },
            tableItemClick:function(row,event,column){
                //将被点击的 item 的 内容同步到input里面
                // console.log(this.input)
                
                // 判断 如果最后不是/ 结尾，那么就将其截断到/ 还需要考虑空的情况
                if(this.input != "" && this.input.charAt(this.input.length - 1 )!='/'){
                    console.log("进入到这个函数中")
                    //截取
                    // index = 0

                    // let a = "123213123121231231"
                    // let index2=a.lastIndexOf('k')
                    // console.log("a")
                    // console.log(index2)
                    // console.log(a.slice(0,index2))
                    let index = this.input.lastIndexOf('/') 
                    console.log(index)
                    if(index == -1){
                        this.input = ""
                    }
                    this.input = this.input.slice(0,index+1)
                }
                if(this.flag==1){
                    this.input+=row.path
                }
                else{
                    this.input+=row.path + "/"
                    this.getDir(this.input)
                }
                this.flag = 0
                // console.log("input:"+this.input)
                // console.log("最后一个："+this.input.charAt(this.input.length - 1 ))
                // console.log(this.input.charAt(this.input.length - 1 )=='/')
            },
            // 同步更改掉currentarray
            getDir:async function(input){
                try{
                    const response = await axiox.get("http://localhost:8888/getDir?dir="+input)
                    currentArray = response.data

                    // console.log("currentArray")
                    // console.log(currentArray)
                    let td = []
                    for(let i=0;i<currentArray.length;i++){
                        let a = {}
                        a.path = currentArray[i]
                        td.push(a)
                    }
                    // console.log(td)
                    // console.log(this.tableData)
                    this.tableData = td
                }
                catch(error){
                    console.error(error) 
                }
            },
            submitButton:async function(){
                console.log(this.searchkey)
                console.log(this.input)
                if(this.searchkey.trim()===""){
                    alert("请输入关键词")
                }
                else{
                    try{
                        // 等待后端的返回的端口已经方式
                        const response = await axiox.get("http://localhost:8888/fileSearch?dir="+this.input+"&&key="+this.searchkey)
                        searchback = response.data
                        // 从后端获取返回的数据
                        // 将返回的数据编程为需要的格式 赋值 
                        console.log(searchback)
                        let a = new Array()
                        for (let key in searchback) {
                            console.log(key) // foo, bar
                            console.log(searchback[key])
                            
                            a.push(this.analysisKeyValue(key,searchback[key]))
                        }
                        this.fileSearchList = a
                    }
                    catch(error){
                        console.error(error) 
                    }
                }
            },
            backToChoose: async function(){
                try{
                    //去掉最后一个路径返回上一级
                    // 1、去掉最后一个"/"
                    this.input = this.input.slice(0,this.input.length-2)
                    // console.log(input)
                    //截取
                    let index = this.input.lastIndexOf('/') 
                    console.log(index)
                    if(index == -1){
                        this.input = ""
                    }
                    else {
                        this.input = this.input.slice(0,index+1)
                    }
                    
                    if(this.input==""){
                        let response = await axiox.get("http://localhost:8888/getDir?dir=getInit")
                        currentArray = response.data
                        for(var i=0;i<currentArray.length;i++){
                        currentArray[i] = currentArray[i].slice(0,currentArray[i].indexOf(":")+1)
                        }
                        console.log(111111111111111)
                        console.log("currentArray")
                        console.log(currentArray)
                        var tableData = []
                        for(let i=0;i<currentArray.length;i++){
                            let a = {}
                            a.path = currentArray[i]
                            tableData.push(a)
                        }
                        console.log(tableData)
                        console.log(this.tableData)
                        this.tableData = tableData
                    }
                    else {
                        let response = await axiox.get("http://localhost:8888/getDir?dir="+this.input)
                        currentArray = response.data
                        let td = []
                        for(let i=0;i<currentArray.length;i++){
                            let a = {}
                            a.path = currentArray[i]
                            td.push(a)
                        }
                        console.log(td)
                        console.log(this.tableData)
                        this.tableData = td
                    }
                }
                catch(error){
                    console.error(error) 
                }
            },
            handleCheckAllChange(val) {
                if(val){
                    let a = new Array()
                    for (let i=0;i<this.fileSearchList.length;i++){
                        a.push(this.fileSearchList[i].id)
                    }
                    this.checkedFlies = a
                }
                else {
                    this.checkedFlies = []
                }
                this.isIndeterminate = false;
            },
            handleCheckedChange(value) {
                let checkedCount = value.length;
                this.checkAll = checkedCount === this.fileSearchList.length;
                this.isIndeterminate = checkedCount > 0 && checkedCount < this.fileSearchList.length;
            },
            download:async function(){
                // 1、汇总所有选中的id
                let a = ""
                for(let i=0;i<this.checkedFlies.length-1;i++){
                    a+=this.checkedFlies[i]+"-"
                }
                a += this.checkedFlies[this.checkedFlies.length-1]
                // console.log(a)
                try{
                    // 等待后端的返回的端口已经方式
                    const response = await axiox.get("http://localhost:8888/download?key="+this.searchkey+"&ids="+a)
                    // searchback = response.data
                    }
                    catch(error){
                        console.error(error) 
                    }




            },
            analysisKeyValue:function(key,value){
                // id=19, dir='C:/Users/Admin/Desktop//计算机志愿.docx'
                    let arr = []
                    arr = key.split(",")
                    for(let i=0;i<arr.length;i++){
                        arr[i] = arr[i].slice(arr[i].indexOf("=")+1)
                    }
                    let filecontext = new Object();
                    filecontext.id = parseInt(arr[0])
                    filecontext.filename = arr[1]
                    filecontext.text = value
                    return filecontext
            }
        }
    }




    
</script>

<style>
    .key-input {
        width: 200px;
        margin: auto;
        margin-bottom: 10px;
    }

    .two-button{
        margin: 20px;
    }
    
    hr.new1 {
        border-top: 1px solid rgba(222, 222, 222,0.3);
    }

    .file-choose-table {
        /* text-align: center; */
        display: flex;
        justify-content:center;
    }

    .sep {
        margin-left: 10px;
        margin-right: 10px;
    }

    .show-checkbox {
        margin-bottom: 20px;

    }
    
</style>

<style scoped>
    .ct >>> .el-checkbox-group {
        font-size:15px 
    }
    .file-choose-table >>> .el-table .el-table__cell{
        /* margin: auto; */
        text-align: center;
    }
    .file-choose-table >>> .el-table__body-wrapper {
        height: 200px; /* 滚动条整体高 必须项 */
        border-right: none;
        overflow-y: scroll;/* overflow-y为了不出现水平滚动条*/
    }
    
    
</style>



