# 前端学习

## CSS代码规范

1. 类名语义化，尽量精短、明确，必须以字母开关命名，且全部字母小写，单词之前统一使用下划线"_"连接
2. 类名嵌套层次尽量不超过三层
3. 尽量避免直接使用元素选择器
4. 属性书写顺序
    布局定位属性：`display` / `position` / `float` / `clear` / `visibility` / `overflow`
    尺寸属性：`width` / `height` / `margin` / `padding` / `border` / `background`
    文本属性：`color` / `font` / `text-decoration` / `text-align` / `vertical-align`
    其他属性（CSS3）：`content` / `cursor` / `border-radius` / `box-shadow` / `text-shadow`
5. 避免使用id选择器
6. 避免使用通配符*和!important

## 移动端技术解决方案

### CSS初始化`normalize.css`

移动端CSS初始化推荐使用[normalize.css](https://necolas.github.io/normalize.css/)

* Normalize.css: 保护了有价值的默认值
* Normalize.css: 修复了浏览器的bug
* Normalize.css: 是模块化的
* Normalize.css: 拥有详细的文档
