import {createI18n, I18nOptions} from "vue-i18n";
import {useLocaleStoreWithOut} from "@/store/modules/locale";
import {setHtmlPageLang} from "@/plugins/vueI18n/helper";
import type {App} from "vue";

export let i18n: ReturnType<typeof createI18n>

const createI18nOptions = async (): Promise<I18nOptions> => {
    const localeStore = useLocaleStoreWithOut();
    const locale = localeStore.getCurrentLocale;
    const localeMap = localeStore.getLocaleMap;
    const defaultLocal = await import(`../../locales/${locale.lang}.ts`);
    const message = defaultLocal.default ?? {};

    setHtmlPageLang(locale.lang)

    localeStore.setCurrentLocale({
        lang: locale.lang
    })

    return {
        legacy: false,
        locale: locale.lang,
        fallbackFormat: locale.lang,
        messages: {
            [locale.lang]: message
        },

        availableLocales: localeMap.map(v => v.lang),
        sync: true,
        silentTranslationWarn: true,
        missingWarn: false,
        silentFallbackWarn: true
    }
}

export const setupI18n = async (app: App<Element>) => {
    const options = await createI18nOptions();
    i18n = createI18n(options);
    app.use(i18n)
}