<template>
  <div id="app">
    <FileKeySearch/>
  </div>
</template>

<script>
import FileKeySearch from './components/FileKeySearch.vue'

export default {
  name: 'App',
  components: {
    FileKeySearch
  },
  data:function(){
    return {
      input:"",
    }
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
