<template>
    <div>
        <!-- <h1>{{ filename }}</h1>
        <p>{{filetext}}</p> -->
        <div class="key-input">
            <el-input placeholder="请输入你要搜索的关键词" v-model="input1"></el-input>
        </div>
        
        <!-- <div>
            <el-cascader :props="props" placeholder="选择你的文件夹地址" v-model="choose"></el-cascader>
        </div> -->

        <div>
            <el-input placeholder="选中文件夹的url地址" v-model="input" :disabled="true"></el-input>
        </div>

        <div>
            <el-table :data="tableData" stripe style="width: 100%" @row-click="tableItemClick">
                <el-table-column prop="path" label="文件夹" width="180" ></el-table-column>
                <el-table-column prop="chooseFolder" label="选择" width="180">
                    <!-- <el-radio v-model="radio" :label="id">选择</el-radio> -->
                    <button @click="tableButtonClick">选择</button>
                </el-table-column>
                <!-- <el-table-column prop="address" label="地址"></el-table-column> -->
        </el-table>
        </div> 
        
        <div>
            <button @click="getUrlFile()">提交</button>
        </div>
        <!-- 展示从后端搜索出来的数据 -->
        <div>
            <li v-for="(user,i) in userlist" :key="user.id"> 索引是{{i}},{{user.username }}</li>    
        </div>

        
    </div>
</template>

<script>
    import axiox from "axios"


    var id = 0;
    // var 
    var currentArray = []
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
                flag:0,
                radio: '0',
                id:++id,
                showlist:[],
                tableData: [],
                input: '',
                input1:"",
                userlist: [
                    {id:1,username:'zhy',sex:"1"},
                    {id:2,username:'zhy1',sex:"1"},
                    {id:3,username:'zhy2',sex:"1"},
                    {id:4,username:'zhy3',sex:"1"},
                ],
                choose: "", 
                props: {
                    lazy: true,
                    lazyLoad(node, resolve) {
                        console.log(node)
                        // const { level } = node;
                        const level = node.level
                        setTimeout(() => {
                            async function getDir(){
                                try{
                                    // 判定如果是空的，那么需要获取盘符
                                    // 将盘符末尾的冒号删去
                                    console.log("我进入了这个函数")
                                    console.log(currentArray.length === 0)
                                    if(level === 0){
                                        console.log("我进入了这个函数2")
                                        const response = await axiox.get("http://localhost:8888/getDir?dir=getInit")
                                        currentArray = response.data
                                        for(var i=0;i<currentArray.length;i++){
                                            currentArray[i] = currentArray[i].slice(0,currentArray[i].indexOf(":")+1)
                                            console.log(currentArray[i])
                                        }
                                        const nodes = currentArray.map(item =>({
                                            value:++id,
                                            label:item,
                                            leaf:level
                                        }))
                                        console.log(nodes)
                                        resolve(nodes);
                                    }
                                    // 如果不是空的。就查看点击的位置，再服务器获取数据，添加到resolve中
                                    else{
                                        var path = [] 
                                        // 获取node的level
                                        // console.log(node.label)
                                        // 它的祖宗们拼接，向服务器询问数据
                                        // const response = await axiox.get("http://localhost:8888/getDir?dir=getInit")
                                        // 将路径存入数组
                                        
                                        // 遍历寻找父亲的label添加到数组
                                        for(var i=level;i>0;i--){
                                            path.push(node.label)
                                            node = node.parent
                                        }
                                        // 反向组成字符串保存为路径
                                        var pathString = ""
                                        for(var i=level-1;i>=0;i--){
                                            pathString += path[i]
                                            pathString += "/"
                                        }
                                        // pathString += path[0]
                                        // 发送请求
                                        console.log("我进入了else函数2")
                                        console.log(node)
                                        console.log(pathString)
                                        const response = await axiox.get("http://localhost:8888/getDir?dir="+pathString)
                                        currentArray = response.data
                                        console.log("currentArray")
                                        console.log(currentArray)
                                        if(currentArray.length===0){
                                            const nodes = currentArray.map(item =>({
                                                value:++id,
                                                label:item,
                                                leaf:level
                                            }))
                                            resolve(nodes);
                                        }
                                        else{
                                            const nodes = currentArray.map(item =>({
                                                value:++id,
                                                label:item,
                                                leaf:level
                                            }))
                                            resolve(nodes);
                                        }
                                    }
                                }
                                catch(error){
                                    console.error(error) 

                                }
                            }
                            getDir()
                        }, 1000);
                    
                    }
                }
            }
        },
        methods:{
            getUrlFile:function(){
                // 获取 input 中的 url 内地址
                // 提交url地址，像服务端获取 JSON 数据来渲染页面
                console.log("我触发了点击事件");
                // 更新渲染表格
                // 并展示
            },
            testClick:function(row,event,column){
                console.log(row)
                console.log(event)
                console.log(column)
                // alert("111")
            },
            testButton:function(e){
                // alert()
                console.log(111111111)
                console.log(e)
                console.log(111111111)
            },
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
                }
                this.flag = 0
                // console.log("input:"+this.input)
                // console.log("最后一个："+this.input.charAt(this.input.length - 1 ))
                // console.log(this.input.charAt(this.input.length - 1 )=='/')
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
</style>




