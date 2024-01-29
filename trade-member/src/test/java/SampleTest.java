import com.ybb.trade.entity.Member;
import com.ybb.trade.mapper.MemberMapper;
import com.ybb.trade.service.MemberService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SampleTest {

    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private MemberService memberService;
    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        Member member = new Member();
        member.setLevel(1);
        member.setMobile("123");
        memberService.save(member);
    }

}