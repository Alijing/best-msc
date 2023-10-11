import {useAppStoreWithOut} from "@/store/modules/app";

const appStore = useAppStoreWithOut();

export const uswPageLoading = () => {
    const loadStart = () => {
        appStore.setPageLoading(true)
    }


    const loadDone = () => {
        appStore.setPageLoading(false)
    }

    return {loadStart, loadDone}
}


