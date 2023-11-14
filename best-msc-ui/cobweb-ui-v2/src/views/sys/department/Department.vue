<script setup lang="tsx">

import {ContentWrap} from '@/components/ContentWrap'
import {Table} from '@/components/Table'
import {Search} from '@/components/Search'
import {ElButton, ElTag} from 'element-plus'
import Update from './components/Update.vue'
import {useTable, UseTableConfig} from "@/hooks/web/useTable";
import {getDepartmentsApi, updateDepartmentsApi, delDepartmentsApi} from '@/api/sys/department'
import {DepartmentNode, DepartmentType} from "@/api/sys/department/types";
import {reactive, ref, unref} from "vue";
import {CrudSchema, useCrudSchemas} from "@/hooks/web/useCrudSchemas";
import {useI18n} from "@/hooks/web/useI18n";
import {Dialog} from "@/components/Dialog";

const {t} = useI18n();

const {tableRegister, tableState, tableMethods} = useTable({
  fetchDataApi: async () => {
    const {currentPage, pageSize} = tableState
    const res = await getDepartmentsApi({
      currentPage: unref(currentPage),
      pageSize: unref(pageSize),
      ...unref(searchParams)
    } as DepartmentType);
    return {
      list: res.data,
      total: res.data.total
    }
  },
  fetchDelApi: async () => {
    const res = delDepartmentsApi(unref(ids));
    return !!res;
  }
} as UseTableConfig);
const {loading, dataList, total, currentPage, pageSize} = tableState;
const {getList, getElTableExpose, delList} = tableMethods;

const searchParams = ref({})
const setSearchParams = (params: any) => {
  searchParams.value = params
  getList()
}

const crudSchemas = reactive<CrudSchema[]>([
  {
    field: 'name',
    label: '部门名称',
    table: {
      slots: {
        default: (data: any) => {
          return <>{data.row.name}</>
        }
      }
    },
  },
  {
    field: 'status',
    label: '状态',
    search: {
      hidden: true
    },
    table: {
      slots: {
        default: (data: any) => {
          const status = data.row.status;
          return (
              <>
                <ElTag type={status === 0 ? 'success' : 'danger'}>
                  {status === 0 ? t('common.enable') : t('common.disable')}
                </ElTag>
              </>
          )
        }
      }
    },
    form: {
      component: 'Select',
      componentProps: {
        options: [
          {
            value: 0,
            label: t('common.enable')
          },
          {
            value: 1,
            label: t('common.disable')
          }
        ]
      }
    }
  },
  {
    field: 'createTime',
    label: '创建时间',
    search: {
      hidden: true
    },
    form: {
      hidden: true
    },
  },
  {
    field: 'remarks',
    label: '备注',
    search: {
      hidden: true
    }
  },
  {
    field: 'action',
    label: '操作',
    width: '380px',
    search: {
      hidden: true
    },
    form: {
      hidden: true
    },
    detail: {
      hidden: true
    },
    table: {
      slots: {
        default: (data: any) => {
          const flag = !data.row.children;
          if (flag) {
            return (
                <>
                  <ElButton type="primary" onClick={() => action(data.row, 'add')}>
                    {t('common.add')}
                  </ElButton>
                  <ElButton type="success" onClick={() => action(data.row, 'edit')}>
                    {t('common.edit')}
                  </ElButton>
                  <ElButton type="danger" onClick={() => action(data.row, 'del')}>
                    {t('common.del')}
                  </ElButton>
                  <ElButton type="primary" onClick={() => action(data.row, 'detail')}>
                    {t('common.detail')}
                  </ElButton>
                </>
            )
          }
          return (
              <>
                <ElButton type="primary" onClick={() => action(data.row, 'add')}>
                  {t('common.add')}
                </ElButton>
                <ElButton type="success" onClick={() => action(data.row, 'edit')}>
                  {t('common.edit')}
                </ElButton>
                <ElButton type="primary" onClick={() => action(data.row, 'detail')}>
                  {t('common.detail')}
                </ElButton>
              </>
          )
        }
      }
    }
  },
]);
// @ts-ignore
const {allSchemas} = useCrudSchemas(crudSchemas);

const dialogVisible = ref(false)
const dialogTitle = ref('')
const actionType = ref('')
const currentRow = ref<DepartmentNode | null>(null);
const saveLoading = ref(false)


const action = (row: DepartmentNode, type: string) => {
  if ('del' === type) {
    delAction(row)
  } else {
    dialogTitle.value = t('common.' + type)
    actionType.value = type
    currentRow.value = row
    dialogVisible.value = true
  }
}

const updateRef = ref<ComponentRef<typeof Update>>()
const save = async (type: number) => {
  let formData = null;
  if (1 == type) {
    const update = unref(updateRef);
    formData = await update?.submit();
    if (formData) {
      saveLoading.value = true
      if (actionType.value === 'add') {
        formData.parentId = formData.id
        formData.id = null
      }
    }
  } else {
    formData = {
      parentId: -1,
      name: '根节点',
      status: 0,
      remarks: '根节点',
    } as DepartmentType
  }
  const res = await updateDepartmentsApi(formData).catch(() => {
  }).finally(() => {
    saveLoading.value = false
  })
  if (res) {
    dialogVisible.value = false
    currentPage.value = 1
    getList()
  }
}

const ids = ref<string[]>([])
const delLoading = ref(false)
const delAction = async (row: DepartmentNode | null) => {
  const elTableExpose = await getElTableExpose()
  ids.value = row
      ? [row.id]
      : elTableExpose?.getSelectionRows().map((v: DepartmentNode) => v.id) || []
  delLoading.value = true
  await delList(unref(ids).length).finally(() => {
    delLoading.value = false
  })
}

</script>

<template>
  <ContentWrap>
    <Search :schema="allSchemas.searchSchema" @search="setSearchParams" @reset="setSearchParams"/>

    <div class="mb-10px">
      <ElButton type="primary" @click="save(0)">{{ t('common.root') }}</ElButton>
    </div>

    <Table v-model:pageSize="pageSize"
           v-model:currentPage="currentPage"
           :columns="allSchemas.tableColumns"
           :data="dataList"
           :loading="loading"
           :pagination="{total:total}"
           @register="tableRegister"/>

    <Dialog v-model="dialogVisible" :title="dialogTitle">
      <Update v-if="actionType === 'add' || actionType === 'edit' " ref="updateRef" :form-schema="allSchemas.formSchema"
              :current-row="currentRow"/>

      <template #footer>
        <ElButton v-if="actionType !== 'detail'" type="primary" :loading="saveLoading" @click="save(1)">
          {{ t('common.save') }}
        </ElButton>
        <ElButton @click="dialogVisible = false">{{ t('common.close') }}</ElButton>
      </template>
    </Dialog>
  </ContentWrap>
</template>

<style scoped>

</style>