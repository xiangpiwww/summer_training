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
    let id = 0;

    export default {
        mounted:function(){
            // 调用接口获取服务器的盘符
            console.log("我获取了所有的盘符")
            this.currentArray = ["c","d"]
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
                currentArray:[],
                choose: '', // 存放的是 value 数组
                path: 'yzz',
                // lazyload方法有两个参数，第一个参数node为当前点击的节点，第二个resolve为数据加载完成的回调(必须调用)。
                // 为了更准确的显示节点的状态，还可以对节点数据添加是否为叶子节点的标志位 (默认字段为leaf，可通过props.leaf修改)，
                // 否则会简单的以有无子节点来判断是否为叶子节点。
                props: {
                    lazy: true,
                    lazyLoad(node, resolve) {
                        
                        // console.log(node)
                        // console.log(node.data)
                        // console.log(resolve)
                        
                        // const { level } = node;
                        const level = node.level
                        // console.log(level)
                        // console.log(Array.from({ length: level + 1 }))
                        
                        setTimeout(() => {
                            // 将当前的 node 的 lable 返回给后端 
                            let path = 'zzz' 
                            // console.log(path)
                            const nodes = Array.from({ length: level + 1 }) //Array.from()方法就是将一个类数组对象或者可遍历对象转换成一个真正的数组，
                                .map(item => ({
                                    value: ++id,
                                    label: `选项${id}`,
                                    leaf: level >= 2
                                }));
                            // 通过调用resolve将子节点数据返回，通知组件数据加载完成
                            resolve(nodes);
                        }, 1000);
                    
                    }
                }
            }
        },
        methods:{
            getUrlFile:function(){
                //给出url地址，像服务端获取 JSON 数据来渲染页面
                console.log("我触发了点击事件");
            },
            


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




