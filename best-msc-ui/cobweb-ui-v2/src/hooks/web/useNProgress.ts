import {useCssVar} from '@vueuse/core'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import {nextTick, unref} from "vue";

const primaryColor = useCssVar('--el-color-primary', document.documentElement)


export const useNProgress = () => {
    NProgress.configure({showSpinner: false})
    const initColor = async () => {
        await nextTick()
        const bar = document.getElementById('nprogress')?.getElementsByClassName('bar')[0] as ElRef
        if (bar) {
            bar.style.backgroundColor = unref(primaryColor.value)
        }
    }

    initColor()

    const start = () => {
        NProgress.start()
    }

    const done = () => {
        NProgress.done()
    }


    return {start, done}
}
