from typing import Optional

from fastapi import FastAPI, applications
from fastapi.openapi.docs import get_swagger_ui_html
from pydantic import BaseModel


def swagger_monkey_patch(*args, **kwargs):
    """
    Wrap the function which is generating the html for the /docs endpoint and
    overwrite the default values for the swagger js and css.
    """
    return get_swagger_ui_html(*args, **kwargs,
                               swagger_js_url="https://cdn.bootcdn.net/ajax/libs/swagger-ui/5.6.2/swagger-ui-bundle.js",
                               swagger_css_url="https://cdn.bootcdn.net/ajax/libs/swagger-ui/5.6.2/swagger-ui.min.css")


# actual monkey patch
applications.get_swagger_ui_html = swagger_monkey_patch

# 创建服务对象
app = FastAPI()


@app.get('/')
async def hello():
    return {"msg": "hello world!"}


class Publication(BaseModel):
    # 接收字符串类型，没有值就默认 None
    title: Optional[str] = None
    abstract: Optional[str] = None


@app.post('/findPublication')
async def find_publication(pc: Publication):
    return {
        "msg": "{}-{}".format(pc.title, pc.abstract)
    }


if __name__ == '__main__':
    import uvicorn

    uvicorn.run(app='main:app', host='192.168.0.68', port=8000, reload=True)
