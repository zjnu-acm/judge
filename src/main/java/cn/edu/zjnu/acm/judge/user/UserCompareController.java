package cn.edu.zjnu.acm.judge.user;

import cn.edu.zjnu.acm.judge.domain.UserProblem;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.ProblemMapper;
import cn.edu.zjnu.acm.judge.mapper.UserMapper;
import cn.edu.zjnu.acm.judge.mapper.UserProblemMapper;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserCompareController {

    @Autowired
    private UserProblemMapper userProblemMapper;
    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/usercmp")
    @SuppressWarnings("CollectionWithoutInitialCapacity")
    public String compare(Model model,
            @RequestParam("uid1") String userId1,
            @RequestParam("uid2") String userId2) {
        checkUser(userId1);
        checkUser(userId2);
        int size = (int) problemMapper.nextId();
        byte[] bytes1 = new byte[size];
        byte[] bytes2 = new byte[size];
        fill(userId1, bytes1);
        fill(userId2, bytes2);
        ArrayList<Integer> a = new ArrayList<>();
        ArrayList<Integer> b = new ArrayList<>();
        ArrayList<Integer> c = new ArrayList<>();
        ArrayList<Integer> d = new ArrayList<>();
        ArrayList<Integer> e = new ArrayList<>();
        ArrayList<Integer> f = new ArrayList<>();
        for (int j = 0; j < size; ++j) {
            if ((bytes1[j] == 1 && bytes2[j] != 1)) {
                a.add(j);
            }
        }
        for (int j = 0; j < size; ++j) {
            if (bytes1[j] != 1 && bytes2[j] == 1) {
                b.add(j);
            }
        }

        for (int j = 0; j < size; ++j) {
            if (bytes1[j] == 1 && bytes2[j] == 1) {
                c.add(j);
            }
        }

        for (int j = 0; j < size; ++j) {
            if (bytes1[j] == 2 && bytes2[j] != 2) {
                d.add(j);
            }
        }

        for (int j = 0; j < size; ++j) {
            if (bytes1[j] != 2 && bytes2[j] == 2) {
                e.add(j);
            }
        }

        for (int j = 0; j < size; ++j) {
            if (bytes1[j] == 2 && bytes2[j] == 2) {
                f.add(j);
            }
        }
        model.addAttribute("uid1", userId1);
        model.addAttribute("uid2", userId2);
        model.addAttribute("a", a);
        model.addAttribute("b", b);
        model.addAttribute("c", c);
        model.addAttribute("d", d);
        model.addAttribute("e", e);
        model.addAttribute("f", f);
        return "users/compare";
    }

    private void checkUser(String uid) {
        if (userMapper.findOne(uid) == null) {
            throw new MessageException("No such user:" + uid, HttpStatus.NOT_FOUND);
        }
    }

    private void fill(String userId, byte[] bytes) {
        List<UserProblem> findAllByUserId = userProblemMapper.findAllByUserId(userId);
        for (UserProblem userProblem : findAllByUserId) {
            bytes[(int) userProblem.getProblem()] = (byte) (userProblem.getAccepted() != 0 ? 1 : 2);
        }
    }

}
