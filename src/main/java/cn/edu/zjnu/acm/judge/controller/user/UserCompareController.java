package cn.edu.zjnu.acm.judge.controller.user;

import cn.edu.zjnu.acm.judge.domain.UserProblem;
import cn.edu.zjnu.acm.judge.mapper.UserProblemMapper;
import cn.edu.zjnu.acm.judge.service.AccountService;
import java.util.BitSet;
import java.util.List;
import java.util.function.BiConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserCompareController {

    private static int[] calc(BitSet a, BiConsumer<BitSet, BitSet> op, BitSet b) {
        BitSet bitset = (BitSet) a.clone();
        op.accept(bitset, b);
        return bitset.stream().toArray();
    }

    @Autowired
    private UserProblemMapper userProblemMapper;
    @Autowired
    private AccountService accountService;

    @GetMapping("/usercmp")
    @SuppressWarnings("CollectionWithoutInitialCapacity")
    public String compare(Model model,
            @RequestParam("uid1") String userId1,
            @RequestParam("uid2") String userId2) {
        accountService.findOne(userId1);
        accountService.findOne(userId2);
        BitSet aac = new BitSet(), awa = new BitSet(), bac = new BitSet(), bwa = new BitSet();
        fill(userId1, aac, awa);
        fill(userId2, bac, bwa);

        model.addAttribute("uid1", userId1);
        model.addAttribute("uid2", userId2);
        model.addAttribute("a", calc(aac, BitSet::andNot, bac));
        model.addAttribute("b", calc(bac, BitSet::andNot, aac));
        model.addAttribute("c", calc(bac, BitSet::and, aac));
        model.addAttribute("d", calc(awa, BitSet::andNot, bwa));
        model.addAttribute("e", calc(bwa, BitSet::andNot, awa));
        model.addAttribute("f", calc(awa, BitSet::and, bwa));
        return "users/compare";
    }

    private void fill(String userId, BitSet ac, BitSet wa) {
        List<UserProblem> findAllByUserId = userProblemMapper.findAllByUserId(userId);
        for (UserProblem userProblem : findAllByUserId) {
            int index = (int) userProblem.getProblem();
            if (userProblem.getAccepted() != 0) {
                ac.set(index);
            } else if (userProblem.getSubmit() != 0) {
                wa.set(index);
            }
        }
    }

}
