package lca.lca2023.repository;


import lca.lca2023.domain.Member;
import org.openlca.core.model.Exchange;
import org.openlca.core.model.Flow;
import org.openlca.core.model.Process;
import org.openlca.nativelib.NativeLib;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import java.util.Map;
import java.util.Optional;
import org.openlca.core.DataDir;
import org.openlca.core.database.FlowDao;

public class JdbcTemplateMemberRepository implements MemberRepository{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateMemberRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Member save(Member member) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("user_info").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("product_name", member.getProductName());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        member.setId(key.intValue());
        return member;
    }

    @Override
    public Optional<Member> findById(int id) {
        NativeLib.loadFrom(DataDir.get().root());	// openLCA DB load
        var db = DataDir.get().openDatabase("test1109_copy7");
        var process = db.getForName(Process.class, "pellet").copy();

        for (Exchange e : process.exchanges){
            Flow f = e.flow;
            if (f.name.contains("Pellet")){
                System.out.println("Pellet find");
            }
        }
        List<Member> result = jdbcTemplate.query("select * from user_info where id = ?", memberRowMapper(), id);
        db.close();
        return result.stream().findAny();
    }

    public List<Member> findAll(){
        return jdbcTemplate.query("select * from user_info", memberRowMapper());
    }

    private RowMapper<Member> memberRowMapper(){
        return (rs, rowNum) -> {    // rs = ResultSet
            Member member = new Member();
            member.setId(rs.getInt("id"));
            member.setProductName(rs.getString("product_name"));
            return member;
        };
    }
}
