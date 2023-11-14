<script setup lang="ts">
import {FormSchema} from "@/components/Form";
import {useForm} from "@/hooks/web/useForm";
import {PropType, reactive, watch} from "vue";
import {DepartmentNode} from "@/api/sys/department/types";
import {useValidator} from "@/hooks/web/useValidator";

const props = defineProps({
  currentRow: {
    type: Object as PropType<Nullable<DepartmentNode>>,
    default: () => null
  },
  formSchema: {
    type: Array as PropType<Nullable<FormSchema[]>>,
    default: () => []
  }
});

const {formRegister, formState, formMethods} = useForm();
const {setValues, getFormData, getElFormExpose} = formMethods;

const {required} = useValidator();

const rules = reactive({
  name: [required()],
  status: [required()]
})

watch(
    () => props.currentRow,
    (currentRow) => {
      if (!currentRow) {
        return
      }
      setValues(currentRow)
    },
    {
      deep: true,
      immediate: true
    }
)

const submit = async () => {
  const elForm = await getElFormExpose();
  const valid = await elForm?.validate().catch((err) => {
    console.log(err)
  })
  if (valid) {
    return await getFormData()
  }
}

defineExpose({submit})

</script>

<template>
  <Form :rules="rules" @register="formRegister" :schema="formSchema"/>
</template>

<style scoped>

</style>