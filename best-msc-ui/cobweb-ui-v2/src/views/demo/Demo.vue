<script setup lang="ts">
import { reactive } from 'vue'
import { Table, TableColumn } from '@/components/Table'

const columns = reactive<TableColumn[]>([
  {
    field: 'title',
    label: 'title'
  },
  {
    field: 'author',
    label: 'author'
  }
])

const data = reactive([
  {
    title: 'title1',
    author: 'author1'
  },
  {
    title: 'title2',
    author: 'author2'
  },
  {
    title: 'title3',
    author: 'author3'
  }
])
</script>

<template>
  <Table :columns="columns" :data="data"/>
</template>