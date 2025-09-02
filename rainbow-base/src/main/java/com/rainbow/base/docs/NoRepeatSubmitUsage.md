# 防重复提交功能使用说明

## 功能介绍

防重复提交功能用于防止用户在短时间内多次提交相同的请求，避免因为用户重复点击按钮或者其他原因导致的数据重复提交问题。

## 实现原理

1. 通过自定义注解 `@NoRepeatSubmit` 标记需要防重复提交的方法
2. 使用拦截器 `NoRepeatSubmitInterceptor` 拦截请求并检查是否重复提交
3. 利用 Redis 缓存存储请求标记，在指定时间内阻止相同请求再次提交

## 使用方法

### 1. 在控制器方法上添加 `@NoRepeatSubmit` 注解

```java
@PostMapping("/submit")
@NoRepeatSubmit
public Result<String> submit(@RequestBody Map<String, Object> params) {
    // 处理提交的数据
    return Result.success("提交成功");
}
```

### 2. 自定义防重复提交时间

```java
@PostMapping("/submit")
@NoRepeatSubmit(interval = 10) // 10秒内不能重复提交
public Result<String> submit(@RequestBody Map<String, Object> params) {
    // 处理提交的数据
    return Result.success("提交成功");
}
```

### 3. 自定义时间单位和提示消息

```java
@PostMapping("/submit")
@NoRepeatSubmit(interval = 1, timeUnit = TimeUnit.MINUTES, message = "请等待1分钟后再次提交")
public Result<String> submit(@RequestBody Map<String, Object> params) {
    // 处理提交的数据
    return Result.success("提交成功");
}
```

## 注解参数说明

| 参数名   | 类型         | 默认值              | 说明                 |
|---------|--------------|-------------------|---------------------|
| interval | int         | 3                 | 防重复提交时间间隔      |
| timeUnit | TimeUnit    | TimeUnit.SECONDS  | 时间单位              |
| message  | String      | "请勿重复提交"      | 重复提交时的提示消息    |

## 示例代码

参考 `com.rainbow.base.controller.ExampleController` 类中的示例方法。