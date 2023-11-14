import request from '@/config/axios'
import type {DepartmentType} from './types'

export const getDepartmentsApi = (data: DepartmentType): Promise<IResponse<DepartmentType>> => {
    return request.post({url: '/sys/department/list', data})
}

export const updateDepartmentsApi = (data: DepartmentType): Promise<IResponse<DepartmentType>> => {
    return request.post({url: '/sys/department/update', data})
}

export const delDepartmentsApi = (ids: string[] | number[]) => {
    return request.post({url: '/sys/department/del', data: {ids}})
}


