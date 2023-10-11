<template>
  <div></div>
</template>

<script setup lang="ts">
import {unref} from "vue";
import {useRouter} from "vue-router";

const {currentRoute, replace} = useRouter();

const {params, query} = unref(currentRoute);
const {path, _redirect_type} = params

Reflect.deleteProperty(params, '_redirect_type')
Reflect.deleteProperty(params, 'path')

const _path = Array.isArray(path) ? path.join('/') : path

if ('name' === _redirect_type) {
  replace({name: _path, query, params})
} else {
  replace({path: _path.startsWith('/') ? _path : '/' + _path, query})
}
</script>
