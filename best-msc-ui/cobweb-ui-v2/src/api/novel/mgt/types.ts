/**
 * 小说信息
 */
export type NovelInfo = {
    id: number
    name: string
    path: string
    createTime: string
    modifyTime: string
}
/**
 * 小说列表查询条件
 */
export type NovelListQuery = {
    currentPage: number
    pageSize: number
    name: string
    batchId: number[]
}






