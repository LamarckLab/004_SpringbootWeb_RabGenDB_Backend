package com.rabies.controller; // 表示这个类属于com.rabies.controller包（命名空间）

// PageHelper 和 PageInfo 是分页插件 PageHelper 的类，用于实现分页功能
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

// 导入自定义的 User 类（实体类）和 UserService 类（业务逻辑层）
import com.rabies.pojo.User;
import com.rabies.service.UserService;

// Spring 框架中的注解，用于标记控制器类及其请求映射
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// Java 的集合类，用于数据返回
import java.util.HashMap;
import java.util.List;

@RestController // 表示这是一个控制器，返回的数据会直接作为 JSON 响应体
public class UserController {

    @Autowired
    private UserService userService;

    // 该接口用于实现: 将User表中的所有用户全部列出来
    @CrossOrigin // 允许前端跨域访问
    @GetMapping("/list") // 绑定GET请求 /list
    public List<User> listUsers() { // 返回的是一个用户集合
        return userService.listUsers(); // 调用服务层中的listUser()方法
    }

    // 该接口用于实现: 向User表中添加一个用户
    @CrossOrigin // 允许前端跨域访问
    @PostMapping("/save") // 绑定POST请求 /save
    public boolean addUser(@RequestBody User user) { // 返回的是一个布尔值, 用来表示是否添加成功
        return userService.addUser(user); // 调用服务层中的addUser()方法
    }

    // 该接口用于实现: 更改User表中某个用户的信息
    @CrossOrigin // 允许前端跨域访问
    @PostMapping("/mod") // 绑定POST请求 /mod
    public boolean modUser(@RequestBody User user) { // 返回的是一个布尔值, 用来表示是否修改成功
        return userService.modUser(user); // 调用服务层中的modUser()方法
    }

    // 该接口用于实现: 删除User表中的某个用户
    @CrossOrigin // 允许前端跨域访问
    @GetMapping("/del") // 绑定GET请求 /del
    public boolean delUser(String username) { // 返回的是一个布尔值, 用来表示是否修改成功
        return userService.delUser(username); // 调用服务层中的delUser()方法
    }

    // 该接口用于实现: 修改User表中某个用户的权限(role)
    @CrossOrigin // 允许前端跨域访问
    @PostMapping("/modAuthority") // 绑定POST请求 /modAuthority
    public boolean modUserAuthority(@RequestBody User user) { // 返回的是一个布尔值, 用来表示是否修改成功
        return userService.modUserAuthority(user); // 调用服务层中的modUserAuthority()方法
    }

    // 该接口用于实现: User表的分页查询
    @CrossOrigin // 允许进行跨域请求
    @GetMapping("/listPage") // 定义了一个Get请求接口
    public HashMap<String, Object> getUserList( // 该方法的返回类型是一个HashMap，用于封装分页数据
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String country) {
        // 启动分页功能
        PageHelper.startPage(pageNum, pageSize);
        // 查询用户列表（条件查询:username、country）
        List<User> users = userService.listByUsernameAndCountry(username, country);
        // PageInfo是PageHelper提供的工具类，在这里用于封装users的信息
        PageInfo<User> pageInfo = new PageInfo<>(users);
        // 创建了一个空的HashMap，用于封装返回给前端的数据
        HashMap<String, Object> result = new HashMap<>();
        // 把分页后的用户列表存入result中的data键中(这部分数据用于渲染表格)
        result.put("data", pageInfo.getList());
        // 把用户总记录数存入result中的total键中(这部分数据用来计算页数)
        result.put("total", pageInfo.getTotal());
        return result;
    }

    // 该接口用于实现: 接收前端传递的用户登录信息并验证
    @CrossOrigin // 运行进行跨域请求
    @PostMapping("/login") // 当前端向/login发送一个POST请求时，会调用这个方法
    public HashMap<String, Object> login(@RequestBody User loginRequest) { // 返回值是一个HashMap, 键是String, 值是Object,
                                                                           // 前端请求被解析为一个User对象
        HashMap<String, Object> result = new HashMap<>(); // 创建了一个空的HashMap, 用于存储返回给前端的结果
        User user = userService.getUserByUsername(loginRequest.getUsername()); // 根据前端提供的no, 查询该User的全部信息
        if (user != null && user.getPassword().equals(loginRequest.getPassword())) { // 如果该用户存在, 且数据库中的密码等于前端提供的密码
            result.put("success", true); // 这里的result.put用于在返回数据中添加一对键值对
            result.put("message", "Login successful");
            result.put("user", user);
        } else {
            result.put("success", false);
            result.put("message", "Incorrect ID or Password");
        }
        return result; // 将result这个HashMap返回给前端
    }

}
