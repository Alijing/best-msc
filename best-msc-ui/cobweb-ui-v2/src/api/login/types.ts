export type UserType = {
    username: string
    password: string
    role: string
    roleId: string
    permissions: string | string[]
    currentPage: number
    pageSize: number
}