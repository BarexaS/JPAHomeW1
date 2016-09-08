package ua.kiev.prog.sample2;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

public class App {
    public static void main( String[] args ) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPATest");
        EntityManager em = emf.createEntityManager();
        try {
            Group group1 = new Group("Course-1");
            Group group2 = new Group("Course-2");
            Client client;
            long gid1, gid2;

            // #1
            System.out.println("------------------ #1 ------------------");

            for (int i = 0; i < 10; i++) {
                client = new Client("Name" + i, i);
                group1.addClient(client);
            }
            for (int i = 0; i < 5; i++) {
                client = new Client("Name" + i, i);
                group2.addClient(client);
            }

            em.getTransaction().begin();
            try {
                em.persist(group1); // save groups with clients
                em.persist(group2);
                em.getTransaction().commit();

                System.out.println("New group id #1: " + (gid1 = group1.getId()));
                System.out.println("New group id #2: " + (gid2 = group2.getId()));
            } catch (Exception ex) {
                em.getTransaction().rollback();
                return;
            }

            Query query = em.createQuery("SELECT g.name FROM Group g");
            List<String> list = query.getResultList();
            for (String grpName : list) {
                query = em.createNamedQuery("Clients.groupCount");
                query.setParameter("grpname",grpName);
                System.out.println("Group name - "+grpName+" this group contains "+query.getSingleResult()+" people");
            }

        } finally {
            em.close();
            emf.close();
        }
    }
}
