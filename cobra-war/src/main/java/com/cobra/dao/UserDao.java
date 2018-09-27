package com.cobra.dao;


import com.cobra.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;

@Mapper
public interface UserDao extends JpaRepository<User,Integer>
{

}
