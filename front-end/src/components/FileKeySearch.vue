<template>
    <div>
        <!-- <h1>{{ filename }}</h1>
        <p>{{filetext}}</p> -->
        <div class="key-input">
            <el-input placeholder="请输入你要搜索的关键词" v-model="input"></el-input>
        </div>
        
        <div>
            <el-cascader :props="props" placeholder="选择你的文件夹地址" v-model="choose"></el-cascader>
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


    let id = 0;
    // var 
    var currentArray = []
    export default {
        mounted:function(){
            // 调用接口获取服务器的盘符
            // axiox.get("http://localhost:8888/getDir?dir=getInit").then(function(response){
            //     // console.log("mxw")
            //     console.log("data="+response.data)
            //     console.log("currentArray"+currentArray)
            //     // for(int i=0;i<response)
            //     currentArray = response.data
                
            //     //将获取的数据放到currentArray里
            // })

            // console.log("我获取了所有的盘符")
            // this.currentArray = ["c","d"]
        },
        data:function(){
            return{
                input: '',
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
                //给出url地址，像服务端获取 JSON 数据来渲染页面
                console.log("我触发了点击事件");
                //并展示
            },
            getThis:function(){
                return this;
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




