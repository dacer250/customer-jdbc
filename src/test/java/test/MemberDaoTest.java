package test;


import com.gupao.edu.jdbc.custome.TestDao;
import com.gupao.edu.jdbc.custome.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@ContextConfiguration(locations = {"classpath*:application-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class MemberDaoTest {
	
	@Autowired
	TestDao testDao;
	
	
	@Test
	public void testSelect(){
		List<Object> ids= Stream.of(1,2).collect(Collectors.toList());
		testDao.selectBetweenIds(ids);
	}



	@Test
	public void testInSelect(){
		List<Long> ids= Stream.of(1L,2L).collect(Collectors.toList());
		testDao.selectTest(ids);
	}


	@Test
	public void testUpdateByKey(){
		User user=new User();
		user.setId(1L);
		user.setName("update");
		try {
			System.out.println(testDao.updateByPrimaryKey(user));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	@Test
	public void testDeleteByPrimaryKey(){
		try {
			System.out.println(testDao.deleteByPrimaryKey(1L));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
/*
	@Test
	@Ignore
	public void testInsert() throws Exception{
		Member entity = new Member();
		entity.setMname("zhangsan");
		memberDao.insert(entity);
	}
	
	@Test
	@Ignore
	public void testInsertAll() throws Exception{
		Member m1 = new Member();
		m1.setMname("m1");
		
		Member m2 = new Member();
		m2.setMname("m2");
		
		Member m3 = new Member();
		m3.setMname("m3");
		
		List<Member> entityList = new ArrayList<Member>();
		entityList.add(m1);
		entityList.add(m2);
		entityList.add(m3);
		
		memberDao.insertAll(entityList);
	}
*/



}
