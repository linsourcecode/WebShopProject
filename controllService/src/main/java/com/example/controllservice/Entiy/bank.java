package com.example.controllservice.Entiy;




import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Table;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Data

@Entity(name = "bank")
public class bank implements Serializable {
    @Id
    @Column(nullable = false) //不允许非空
    private String id;
    @Column(name="money")
    private Double money;

    public bank() {
    }
}
