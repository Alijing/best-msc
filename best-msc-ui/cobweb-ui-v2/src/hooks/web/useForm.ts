import {nextTick, ref, unref} from "vue";
import type {Form, FormExpose} from "@/components/Form";
import {FormProps, FormSchema, FormSetProps} from "@/components/Form";
import type {ElForm, ElFormItem} from "element-plus";

export const useForm = () => {
    // Form 实例
    const formRef = ref<typeof Form & FormExpose>();
    // ElForm 实例
    const elFormRef = ref<ComponentRef<typeof ElForm>>();
    /**
     * @param ref Form 实例
     * @param elRef ElForm 实例
     */
    const register = (ref: typeof Form & FormExpose, elRef: ComponentRef<typeof ElForm>) => {
        formRef.value = ref
        elFormRef.value = elRef
    }

    const getForm = async () => {
        await nextTick()
        const form = unref(formRef);
        if (!form) {
            console.error('The form is not registered. Please use the `register` method to register ')
        }
        return form
    }

    // 一些内置的方法
    const methods = {
        /**
         * @description 设置 form 组件的 props
         * @param props form 组件的 props
         */
        setProps: async (props: FormProps = {}) => {
            const form = await getForm();
            form?.setProps(props)
            if (props.model) {
                form?.setValues(props.model)
            }
        },
        /**
         * @description 设置 form 的值
         * @param data 需要设置的数据
         */
        setValues: async (data: Recordable) => {
            const form = await getForm();
            form?.setValues(data)
        },
        /**
         * @description 设置 schema
         * @param schemaProps 需要设置的 schemaProps
         */
        setSchema: async (schemaProps: FormSetProps[]) => {
            const form = await getForm();
            form?.setSchema(schemaProps)
        },
        /**
         *@description 新增 schema
         * @param formSchema 需要新增的数据
         * @param index 在哪里新增
         */
        addSchema: async (formSchema: FormSchema, index?: number) => {
            const form = await getForm();
            form?.addSchema(formSchema, index)
        },
        /**
         * @description 删除 schema
         * @param field 删除那个数据
         */
        delSchema: async (field: string) => {
            const form = await getForm();
            form?.delSchema(field)
        },
        /**
         * @description 获取表单数据
         * @returns form data
         */
        getFormData: async <T = Recordable>(): Promise<T> => {
            const form = await getForm();
            return form?.formModel as T
        },
        /**
         * @description 获取表单组件的实例
         * @param field 表单唯一标识
         * @returns component instance
         */
        getComponentExpose: async (field: string) => {
            const form = await getForm();
            return form?.getComponentExpose(field)
        },
        /**
         * @description 获取 formItem 组件的实例
         * @param field 表单项唯一标识
         * @returns formItem instance
         */
        getFormItemExpose: async (field: string) => {
            const form = await getForm();
            return form?.getFormItemExpose(field) as ComponentRef<typeof ElFormItem>
        },
        /**
         * @description 获取 ElForm 组件的实例
         * @returns ElForm instance
         */
        getElFormExpose: async () => {
            await getForm()
            return unref(elFormRef)
        },
        getFormExpose: async () => {
            await getForm()
            return unref(formRef)
        }
    }
    return {formRegister: register, formMethods: methods}
}

