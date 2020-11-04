package org.isf.dao;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Data
@Table(name="usergroup")
@EntityListeners(AuditingEntityListener.class)
@AttributeOverrides({
        @AttributeOverride(name="createdBy", column=@Column(name="UG_CREATED_BY")),
        @AttributeOverride(name="createdDate", column=@Column(name="UG_CREATED_DATE")),
        @AttributeOverride(name="lastModifiedBy", column=@Column(name="UG_LAST_MODIFIED_BY")),
        @AttributeOverride(name="active", column=@Column(name="UG_ACTIVE")),
        @AttributeOverride(name="lastModifiedDate", column=@Column(name="UG_LAST_MODIFIED_DATE"))
})
public class UserGroup
{
    @Id
    @Column(name="UG_ID_A")
    private String code;

    @Column(name="UG_DESC")
    private String desc;

    @Transient
    private volatile int hashCode = 0;

    public UserGroup(int hashCode) {
        this.hashCode = hashCode;
    }

    public UserGroup(String code, String desc){
        this.code=code;
        this.desc=desc;
    }
    public UserGroup(){
        this("","");
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String toString(){
        return getCode();
    }

    @Override
    public boolean equals(Object anObject) {
        return (anObject == null) || !(anObject instanceof UserGroup) ? false
                : (getCode().equalsIgnoreCase(
                ((UserGroup) anObject).getCode()) && getDesc()
                .equalsIgnoreCase(
                        ((UserGroup) anObject).getDesc()));
    }

    @Override
    public int hashCode() {
        if (this.hashCode == 0) {
            final int m = 23;
            int c = 133;

            c = m * c + code.hashCode();

            this.hashCode = c;
        }

        return this.hashCode;
    }
}
