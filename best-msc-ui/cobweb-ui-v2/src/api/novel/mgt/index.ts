import type {NovelInfo, NovelListQuery} from "./types";
import request from "@/config/axios";

export const novelListApi = (data: NovelListQuery): Promise<IResponse<NovelInfo>> => {
    return request.post({url: '/novel/list', data})
}
