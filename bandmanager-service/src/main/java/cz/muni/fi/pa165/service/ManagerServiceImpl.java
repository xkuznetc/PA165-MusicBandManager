package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.dao.ManagerDao;
import cz.muni.fi.pa165.entity.Band;
import cz.muni.fi.pa165.entity.BandInvite;
import cz.muni.fi.pa165.entity.Manager;
import cz.muni.fi.pa165.entity.Tour;
import cz.muni.fi.pa165.utils.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Matej Sojak 433294
 */
@Service
public class ManagerServiceImpl implements ManagerService {

    @Inject
    private ManagerDao managerDao;

    @Override
    public void registerManager(Manager m, String unencryptedPassword) {
        if(unencryptedPassword == null || unencryptedPassword.length() < 5){
            throw new RuntimeException("The provided password is too short");
        }
        m.setPassword(Validator.createHash(unencryptedPassword));
        managerDao.create(m);
    }

    @Override
    public List<Manager> getAllManagers() {
        return managerDao.findAll();
    }

    @Override
    public Manager findManagerById(Long id) {
        return managerDao.findById(id);
    }

    @Override
    public Manager findManagerByName(String name) {
        return managerDao.findByName(name);
    }

    @Override
    public void addManagedBand(Manager m, Band b) {
        if(m == null || b == null ){
            throw new RuntimeException("Manager or band is null");
        }
        if(managerDao.findById(m.getId())==null){
            throw new RuntimeException("This action cannot be performed on a non-existent manager.");
        }
        if(b.getManager()!=null){
            throw new RuntimeException("This band is already managed.");
        }
        m.addBand(b);
        managerDao.update(m);
    }

    @Override
    public void addTour(Manager m, Tour t) {
        if(m == null || t == null ){
            throw new RuntimeException("Manager or tour is null");
        }
        if(managerDao.findById(m.getId())==null){
            throw new RuntimeException("This action cannot be performed on a non-existent manager.");
        }
        m.addTour(t);
        managerDao.update(m);
    }

    @Override
    public void addBandInvite(Manager m, BandInvite b) {
        if(m == null || b == null ){
            throw new RuntimeException("Manager or band is null");
        }
        if(managerDao.findById(m.getId())==null){
            throw new RuntimeException("This action cannot be performed on a non-existent manager.");
        }
        m.addBandInvite(b);
        managerDao.update(m);
    }

    @Override
    public void cancelManagedBand(Manager m, Band b) {
        if(m == null || b == null ){
            throw new RuntimeException("Manager or band is null");
        }
        if(managerDao.findById(m.getId())==null){
            throw new RuntimeException("This action cannot be performed on a non-existent manager.");
        }
        if(!m.getBands().contains(b)){
            throw new RuntimeException("The manager does not manage the provided band.");
        }
        m.removeBand(b);
        managerDao.update(m);
    }

    @Override
    public void cancelTour(Manager m, Tour t) {
        if(m == null || t == null ){
            throw new RuntimeException("Manager or tour is null");
        }
        if(managerDao.findById(m.getId())==null){
            throw new RuntimeException("This action cannot be performed on a non-existent manager.");
        }
        if(!m.getTours().contains(t)){
            throw new RuntimeException("The manager does not manage the provided tour.");
        }
        m.removeTour(t);
        managerDao.update(m);
    }

    @Override
    public void cancelBandInvite(Manager m, BandInvite b) {
        if(m == null || b == null ){
            throw new RuntimeException("Manager or band invite is null");
        }
        if(managerDao.findById(m.getId())==null){
            throw new RuntimeException("This action cannot be performed on a non-existent manager.");
        }
        if(!m.getBandInvites().contains(b)){
            throw new RuntimeException("The manager does not have this pending band invite.");
        }
        m.removeBandInvite(b);
        managerDao.update(m);
    }

    @Override
    public boolean authenticate(Manager m, String password) {
        return Validator.validatePassword(password,m.getPassword());
    }

    @Override
    public void changeEmail(Manager m, String newEmail) {
        if(newEmail == null || !Validator.validateEmail(newEmail)){
            throw new RuntimeException("The provided email is invalid!");
        }
        if(managerDao.findById(m.getId())==null){
            throw new RuntimeException("This action cannot be performed on a non-existent manager.");
        }
        m.setEmail(newEmail);
        managerDao.update(m);
    }

    @Override
    public void changePassword(Manager m, String newPassword) {
        if(newPassword == null || newPassword.length() < 5){
            throw new RuntimeException("The provided password is too short");
        }
        if(managerDao.findById(m.getId())==null){
            throw new RuntimeException("This action cannot be performed on a non-existent manager.");
        }
        m.setPassword(Validator.createHash(newPassword));
        managerDao.update(m);
    }
}
