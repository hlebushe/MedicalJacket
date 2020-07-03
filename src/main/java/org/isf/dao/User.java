package org.isf.dao;

import lombok.Data;
import org.isf.dao.UserGroup;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Table(name="USER")
@AttributeOverrides({
        @AttributeOverride(name="createdBy", column=@Column(name="US_CREATED_BY")),
        @AttributeOverride(name="createdDate", column=@Column(name="US_CREATED_DATE")),
        @AttributeOverride(name="lastModifiedBy", column=@Column(name="US_LAST_MODIFIED_BY")),
        @AttributeOverride(name="active", column=@Column(name="US_ACTIVE")),
        @AttributeOverride(name="lastModifiedDate", column=@Column(name="US_LAST_MODIFIED_DATE"))
})
public class User
{
    @Id
    @Column(name="US_ID_A")
    private String userName;

    @NotNull
    @ManyToOne
    @JoinColumn(name="US_UG_ID_A")
    private UserGroup userGroupName;

    @NotNull
    @Column(name="US_PASSWD")
    private String passwd;

    @Column(name="US_DESC")
    private String desc;

    @Transient
    private volatile int hashCode = 0;


    public User(){
    }

    public User(String aName, UserGroup aGroup, String aPasswd, String aDesc){
        this.userName = aName;
        this.userGroupName = aGroup;
        this.passwd = aPasswd;
        this.desc = aDesc;
    }

    public String toString(){
        return getUserName();
    }

    @Override
    public boolean equals(Object anObject) {
        return (anObject == null) || !(anObject instanceof User) ? false
                : (getUserName().equalsIgnoreCase(
                ((User) anObject).getUserName()) && getDesc()
                .equalsIgnoreCase(
                        ((User) anObject).getDesc()));
    }

    @Override
    public int hashCode() {
        if (this.hashCode == 0) {
            final int m = 23;
            int c = 133;

            c = m * c + userName.hashCode();

            this.hashCode = c;
        }

        return this.hashCode;
    }

}
