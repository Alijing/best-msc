export type DepartmentType = {
    currentPage: number
    pageSize: number
    total: number
}

export type DepartmentNode = {
    id: number
    name: string
    parentId: number
    status: string
    remark: string
    createTime: string
    revisionTime: string
    children: DepartmentNode[]
}